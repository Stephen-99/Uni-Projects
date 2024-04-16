package edu.curtin.api;

import java.util.Locale;
import java.util.ResourceBundle;

public interface CalendarAPI 
{
    void addEvent(CalendarEvent event);
    void addEvent(String title, int year, int month, int day);
    void addEvent(String title, int year, int month, int day, int hour, int minute, int second, int duration);
    void registerEventHandler(CalendarEventHandler eventHandler);
    Locale getLocale();
    ResourceBundle getResourceBundle();
}

