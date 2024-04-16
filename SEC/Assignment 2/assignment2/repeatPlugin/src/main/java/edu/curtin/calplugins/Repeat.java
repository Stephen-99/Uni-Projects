package edu.curtin.calplugins;

import edu.curtin.api.*;
import java.util.Dictionary;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Repeat implements CalendarPlugin
{
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private int durationMinutes;
    private int daysBetweenRepeats;

    //assumes the correct fields are there
    public Repeat(Dictionary<String, String> args)
    {
        title = args.get("title");
        String[] dateStr = args.get("startDate").split("-");
        startDate = LocalDate.of(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]), Integer.parseInt(dateStr[2]));

        startTime = null;
        String dictRes = args.get("startTime");
        if (dictRes != null)
        {
            String[] timeStr = dictRes.split(":");
            startTime = LocalTime.of(Integer.parseInt(timeStr[0]), Integer.parseInt(timeStr[1]), Integer.parseInt(timeStr[2]));
            durationMinutes = Integer.parseInt(args.get("duration"));
        }

        daysBetweenRepeats = Integer.parseInt(args.get("repeat"));
    }

    @Override
    public void start(CalendarAPI api) 
    {
        if (startTime == null)
        {
            int ii = 0;
            while (startDate.until(startDate.plusDays(ii * daysBetweenRepeats), ChronoUnit.YEARS) < 1)
            {
                api.addEvent(new AllDayEvent(title, startDate.plusDays(ii * daysBetweenRepeats)));
                ii++;
            }
        }
        else
        {
            int ii = 0;
            while (startDate.until(startDate.plusDays(ii * daysBetweenRepeats), ChronoUnit.YEARS) < 1)
            {
                api.addEvent(new TimeOfDayEvent(title, startDate.plusDays(ii * daysBetweenRepeats), startTime, Duration.ofMinutes(durationMinutes), api.getResourceBundle(), api.getLocale()));
                ii++;
            }
        }
    }
}