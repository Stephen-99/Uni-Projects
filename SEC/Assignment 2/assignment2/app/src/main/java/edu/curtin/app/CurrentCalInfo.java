package edu.curtin.app;

import java.util.*;

import edu.curtin.api.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CurrentCalInfo implements Iterable<CurrentCalInfo.CalRow>
{
    @SuppressWarnings("PMD") //Capitals are convention for constants
    private final int NUM_DAYS = 7;
    @SuppressWarnings("PMD") //Capitals are convention for constants
    private final int NUM_HRS_PER_DAY = 24;

    public class CalRow
    {
        private String rowHeading;
        private String[] row;
        private boolean isEmpty;

        public CalRow(String rowHeading)
        {
            this.rowHeading = rowHeading;
            resetRow();
        }

        public String getRowHeading() {
            return rowHeading;
        }
        
        public String[] getRow() {
            return row;
        }

        public void addInfo(int day, String info)
        {
            if (row[day].equals(""))
            {
                isEmpty = false;
                row[day] = info;
            }
            else
            {
                row[day] += "\n\n" + info;
            }
        }

        public boolean isEmpty()
        {
            return isEmpty;
        }

        private void resetRow()
        {
            isEmpty = true;

            row = new String[NUM_DAYS];
            for (int ii = 0; ii < NUM_DAYS; ii++)
            {
                row[ii] = "";
            }
        }
    }

    private CalRow[] currentInfo;
    private LocalDate currentDate;

    public CurrentCalInfo(ResourceBundle bundle, Locale locale)   
    {
        currentDate = LocalDate.now();

        //Initialise the array to empty strings.
        currentInfo = new CalRow[NUM_HRS_PER_DAY + 1];
        currentInfo[0] = new CalRow(bundle.getString("all_day"));
        String heading;
        for (int ii = 0; ii < NUM_HRS_PER_DAY; ii++)
        {
            //creating a row for each hour, making sure to localise the heading.
            heading = LocalTime.of(ii, 0).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale));
            currentInfo[ii + 1] = new CalRow(heading);
        }
    }

    public LocalDate getCurrentDate()
    {
        return currentDate;
    }


    private void addInfo(int day, int hour, String info)
    {
        // + 1 to hour to account for the extra all-day events row
        currentInfo[hour+1].addInfo(day, info);
    }
    
    private void addEvent(CalendarEvent event)
    {
        long daysTillEvent = currentDate.until(event.getStartDate(), ChronoUnit.DAYS);
        //add event if it occurs within NUM_DAYS days from now.
        if (0 <= daysTillEvent && daysTillEvent < NUM_DAYS)
        {
            addInfo((int)daysTillEvent, event.getStartHour(), event.getDisplayText());
        }
    }

    public void addEvents(List<CalendarEvent> events)
    {
        events.sort(null);
        for (CalendarEvent event : events) 
        {
            addEvent(event);
        }
    }

    private void clearEventInfo()
    {
        for (CalRow calRow : currentInfo) 
        {
            calRow.resetRow(); 
        }
    }

    public void moveForward(Period amount, List<CalendarEvent> events)
    {
        currentDate = currentDate.plus(amount);
        clearEventInfo();
        addEvents(events);
    }

    @Override
    public Iterator<CurrentCalInfo.CalRow> iterator() 
    {
        Iterator<CurrentCalInfo.CalRow> iter = new Iterator<CurrentCalInfo.CalRow>() 
        {
            private int currentIndex = 0;

            //max size of array is NUM_HRS_PER_DAY
            @Override
            public boolean hasNext() 
            {
                //only look for non-empty rows
                while (currentIndex < NUM_HRS_PER_DAY && currentInfo[currentIndex].isEmpty())
                {
                    currentIndex++;
                }

                return currentIndex < NUM_HRS_PER_DAY;
            }

            @Override
            public CurrentCalInfo.CalRow next() 
            {
                //call hasNext before calling next so we get the right index
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                
                //returns row at current index and increments it to the next element
                return currentInfo[currentIndex++];
            }
            
        };
        return iter;
    }
}
