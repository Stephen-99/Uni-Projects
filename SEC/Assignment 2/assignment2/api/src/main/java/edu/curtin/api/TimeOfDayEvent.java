package edu.curtin.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import java.util.ResourceBundle;
import java.util.Locale;
import java.text.NumberFormat;

public final class TimeOfDayEvent implements CalendarEvent, Comparable<CalendarEvent>
{
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private Duration duration;
    private Locale locale;
    private ResourceBundle bundle;

    public TimeOfDayEvent(String title, LocalDate startDate, LocalTime startTime, Duration duration, ResourceBundle bundle, Locale locale)
    {
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.bundle = bundle;
        this.locale = locale;
    }

    @Override
    public String getDisplayText() 
    {
        return title + "\n" 
        + startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)) + "\n" 
        + NumberFormat.getInstance(locale).format(duration.get(ChronoUnit.SECONDS) / 60) + " " + bundle.getString("minutes_long");
    }

    @Override
    public LocalDate getStartDate() 
    {
        return startDate;
    }

    @Override
    public LocalTime getStartTime()
    {
        return startTime;
    }

    @Override
    public int getStartHour() 
    {
        return startTime.getHour();
    }

    public Duration getDuration()
    {
        return duration;
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
        if (hoursDif != 0)
        {
            return hoursDif;
        }
        return startTime.compareTo(((TimeOfDayEvent)otherEvent).getStartTime());
    }

    @Override
    public String getTitle()
    {
        return title;
    }
}

