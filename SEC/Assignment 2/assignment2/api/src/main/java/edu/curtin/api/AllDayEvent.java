package edu.curtin.api;

import java.time.LocalDate;
import java.time.LocalTime;

public final class AllDayEvent implements CalendarEvent, Comparable<CalendarEvent>
{
    private String title;
    private LocalDate startDate;

    public AllDayEvent(String title, LocalDate startDate)
    {
        this.title = title;
        this.startDate = startDate;
    }

    @Override
    public String getDisplayText() 
    {
        return title;
    }

    @Override
    public LocalDate getStartDate() 
    {
        return startDate;
    }

    @Override
    public int getStartHour() 
    {
        //Doesn't start at an hour of the day, but essentially before the day begins, hence -1
        return -1;
    }

    @Override
    public int compareTo(CalendarEvent otherEvent) 
    {
        int daysDif = otherEvent.getStartDate().until(startDate).getDays();
        if (daysDif != 0)
        {
            return daysDif;
        }
        int hoursDif = otherEvent.getStartHour() - getStartHour();
        return hoursDif;
    }

    @Override
    public LocalTime getStartTime() 
    {
        return LocalTime.of(0, 0);
    }

    @Override
    public String getTitle() 
    {
        return title;
    }
    
}
