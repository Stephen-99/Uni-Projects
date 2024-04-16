package edu.curtin.app;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;

import edu.curtin.api.AllDayEvent;
import edu.curtin.api.PluginException;
import edu.curtin.api.TimeOfDayEvent;

public class TempHardCodedCalendar 
{
    public static void setupCalendar(Calendar calendar)
    {
        //Test events
        calendar.addEvent(new AllDayEvent("Fav all day event", LocalDate.now()));
        calendar.addEvent(new AllDayEvent("2nd all day event", LocalDate.now().plusDays(3)));
        calendar.addEvent(new TimeOfDayEvent("Test notification event", LocalDate.now(), LocalTime.now().plus(Duration.ofSeconds(5)), Duration.ofMinutes(100), calendar.getResourceBundle(), calendar.getLocale()));
        calendar.addEvent(new TimeOfDayEvent("Breakfast", LocalDate.now(), LocalTime.of(9, 0), Duration.ofMinutes(30), calendar.getResourceBundle(), calendar.getLocale()));
        calendar.addEvent(new TimeOfDayEvent("Date night", LocalDate.now().plusDays(5), LocalTime.of(18, 15), Duration.ofHours(400), calendar.getResourceBundle(), calendar.getLocale()));
        calendar.addEvent(new TimeOfDayEvent("Buy roses", LocalDate.now().plusDays(5), LocalTime.of(18, 00), Duration.ofMinutes(10), calendar.getResourceBundle(), calendar.getLocale()));

        //Notify plugin
        Hashtable<String, String> notifValues = new Hashtable<>();
        notifValues.put("text", "notif");
        
        try 
        {
            calendar.startPlugin("edu.curtin.calplugins.Notify", notifValues);
        } 
        catch (PluginException e) 
        {
            System.out.println(e.getMessage());
        }

        //Repeat plugin with all-day event
        Hashtable<String, String> repeatValues = new Hashtable<>();
        repeatValues.put("title", "repeating event");
        repeatValues.put("startDate", "2023-11-12");
        repeatValues.put("repeat", "2");
        
        try 
        {
            calendar.startPlugin("edu.curtin.calplugins.Repeat", repeatValues);
        } 
        catch (PluginException e) 
        {
            System.out.println(e.getMessage());
        }

        //Repeat plugin with time-of-day event
        Hashtable<String, String> repeatValues2 = new Hashtable<>();
        repeatValues2.put("title", "repeating TOD event");
        repeatValues2.put("startDate", "2023-11-13");
        repeatValues2.put("startTime", "19:14:13");
        repeatValues2.put("duration", "11");
        repeatValues2.put("repeat", "7");
        
        try 
        {
            calendar.startPlugin("edu.curtin.calplugins.Repeat", repeatValues2);
        } 
        catch (PluginException e) 
        {
            System.out.println(e.getMessage());
        }

        //extra script for testing
        calendar.runPythonScript("print('hello world, from python!\\n\\n')\n" +
            "api.addEvent(\"python event\", 2023, 11, 15)\n" + 
            "api.addEvent(\"python event 2\", 2023, 11, 15, 14, 12, 0, 10)\n" + 
            
            "class EH(CalendarEventHandler):" +
                "\n\tdef eventStarted(self, event):" +
                    "\n\t\tprint(\"EVENT HAS STARTED IN PYTHON:\\n\" + event.getDisplayText() + \"\\n\\n\")\n" +
            "\napi.registerEventHandler(EH())");

        //public holidays/important dates script
        calendar.runPythonScript("api.addEvent(\"Christmas\", 2023, 12, 25)\n" +
            "api.addEvent(\"My birthday!!\", 2024, 6, 26)\n" +
            "api.addEvent(\"New Year's Eve\", 2023, 12, 31)\n" +
            "api.addEvent(\"New Year's day\", 2024, 1, 1)");
    }
}
