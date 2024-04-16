package edu.curtin.api;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CalendarEvent
{
    public LocalDate getStartDate();
    public LocalTime getStartTime();
    public int getStartHour();

    public String getTitle();
    public String getDisplayText();
}
