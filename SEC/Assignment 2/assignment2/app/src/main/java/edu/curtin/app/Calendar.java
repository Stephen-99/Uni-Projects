package edu.curtin.app;

import org.python.util.PythonInterpreter;

import edu.curtin.api.*;

import java.util.*;

import javax.swing.JOptionPane;

import java.lang.reflect.InvocationTargetException;
import java.text.Normalizer;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Calendar implements CalendarAPI
{
    private CurrentCalInfo calInfo;
    private List<CalendarEvent> events;
    private List<CalendarEventHandler> eventHandlers;
    private Timer timer;
    private CalendarGrid grid;
    private Locale locale;
    private ResourceBundle bundle;
    

    public Calendar()
    {
        timer = new Timer();
        events = new ArrayList<>();
        eventHandlers = new ArrayList<>();
    }

    public void initCalendar(ResourceBundle bundle, Locale locale)
    {
        this.bundle = bundle;
        this.locale = locale;
        calInfo = new CurrentCalInfo(bundle, locale);
        grid = new CalendarGrid(calInfo, locale);
    }

    @Override
    public void addEvent(CalendarEvent event)
    {
        events.add(event);
        scheduleEventNotification(event);
    }

    @Override
    public void addEvent(String title, int year, int month, int day) 
    {
        addEvent(new AllDayEvent(title, LocalDate.of(year, month, day)));
    }

    @Override
    public void addEvent(String title, int year, int month, int day, int hour, int minute, int second, int duration) 
    {
        addEvent(new TimeOfDayEvent(title, LocalDate.of(year, month, day), LocalTime.of(hour, minute), Duration.ofMinutes(duration), bundle, locale));
    }

    public void showCalendar()
    {
        calInfo.addEvents(events);
        grid.resetRows(calInfo);
        grid.displayGrid(bundle);
    }

    public void shiftCurrentDate(Period amount)
    {
        calInfo.moveForward(amount, events);
        resetEventNotifications();
        grid.resetRows(calInfo);
        grid.displayGrid(bundle);
    }

    public void resetCurrentDate()
    {
        //reset the current date to today
        shiftCurrentDate(calInfo.getCurrentDate().until(LocalDate.now()));
    }

    public void searchForEvent(String searchTerm)
    {
        boolean foundEvent = false;
        searchTerm = Normalizer.normalize(searchTerm.toLowerCase(), Normalizer.Form.NFKC);
        events.sort(null);

        LocalDate aYearAway = calInfo.getCurrentDate().plusYears(1);
        for (CalendarEvent event : events) 
        {
            if (calInfo.getCurrentDate().until(event.getStartDate(), ChronoUnit.DAYS) < 0)
            {
                //event occurs before current date
                continue;
            }
            if (event.getStartDate().until(aYearAway, ChronoUnit.DAYS) < 0)
            {
                //event is more than a year away, as will be the rest of the events in the list
                break;
            }
            String normalizedEventTitle = Normalizer.normalize(event.getTitle().toLowerCase(), Normalizer.Form.NFKC);
            if (normalizedEventTitle.contains(searchTerm))
            {
                JOptionPane.showMessageDialog(null, bundle.getString("event_found") + event.getDisplayText());
                shiftCurrentDate(calInfo.getCurrentDate().until(event.getStartDate()));
                foundEvent = true;
                break;
            }
        }
        if (!foundEvent)
        {
            JOptionPane.showMessageDialog(null, bundle.getString("event_not_found"));
        }
    }

    @Override
    public void registerEventHandler(CalendarEventHandler eventHandler) 
    {
        eventHandlers.add(eventHandler);
    }

    public void cleanup()
    {
        timer.cancel();
    }

    @Override
    public Locale getLocale()
    {
        return locale;
    }

    @Override
    public ResourceBundle getResourceBundle()
    {
        return bundle;
    }

    public void startPlugin(String pluginName, Dictionary<String, String> args) throws PluginException
    {
        Class<?> pluginClass;
        try
        {
            pluginClass = Class.forName(pluginName);
            CalendarPlugin pluginObj = (CalendarPlugin)pluginClass.getConstructor(Dictionary.class).newInstance(args);
            pluginObj.getClass().getMethod("start", CalendarAPI.class).invoke(pluginObj, this);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new PluginException(bundle.getString("plugin_fail") + ": " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException") //the script could almost any exception type to occur
    public void runPythonScript(String pythonScript)
    {
        try (PythonInterpreter interpreter = new PythonInterpreter())
        {
            interpreter.set("api", this);
            interpreter.set("CalendarEventHandler", CalendarEventHandler.class);
            interpreter.exec(pythonScript);
        } 
        catch (Exception e) 
        {
            System.out.println(bundle.getString("script_fail") + ": " + e.getMessage());
        }
    }

    private void scheduleEventNotification(CalendarEvent event)
    {
        LocalDateTime currentDateTime = LocalDateTime.of(calInfo.getCurrentDate(), LocalTime.now());
        LocalDateTime eventDateTime = LocalDateTime.of(event.getStartDate(), event.getStartTime());
        long millisTillEvent = currentDateTime.until(eventDateTime, ChronoUnit.MILLIS);
        
        //If the event happens in the future, schedule a notification task
        if (millisTillEvent > 0)
        {
            timer.schedule(new NotifyEventHandlers(event), millisTillEvent);
        }
    }

    private void resetEventNotifications()
    {
        timer.cancel();
        timer = new Timer();
        for (CalendarEvent event : events) 
        {
            scheduleEventNotification(event);
        }
    }

    private class NotifyEventHandlers extends TimerTask
    {
        private CalendarEvent event;
        public NotifyEventHandlers(CalendarEvent event)
        {
            this.event = event;
        }

        @Override
        public void run()
        {
            notifyEventHandlers(event);
        }
    }

    private void notifyEventHandlers(CalendarEvent event)
    {
        for (CalendarEventHandler eventHandler : eventHandlers) 
        {
            eventHandler.eventStarted(event);
        }
    }
}

