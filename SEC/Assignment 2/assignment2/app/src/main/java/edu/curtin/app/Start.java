package edu.curtin.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.CharacterCodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Start 
{
    public static void main(String[] args)
    { 
        //dependency injection
        Calendar calendar = new Calendar();
        UIHandler uiHandler = new UIHandler(calendar);
        Locale locale = uiHandler.initUIHandler();
        ResourceBundle bundle = ResourceBundle.getBundle("edu.curtin.app.bundle", locale);
        calendar.initCalendar(bundle, locale);
      
        if (args.length == 0)
        {
            calendar.cleanup();
            System.out.println(bundle.getString("invalid_args"));
            return;
        }

        //There's no actual parsing going on. Just for demo of the encodings.
        parseInputFile(args[0], bundle);
        TempHardCodedCalendar.setupCalendar(calendar);

        uiHandler.runUI();
        calendar.cleanup();
    } 

    private static void parseInputFile(String filename, ResourceBundle bundle)
    {
        BufferedReader br = openCalendarFile(filename);
        if (br == null)
        {
            System.out.println(bundle.getString("file_error"));
            return;
        }

        CalendarParser parser = new CalendarParser(br);
        try
        {
            parser.parse();
        }
        catch (ParseException e)
        {
            System.out.println(bundle.getString("parser_error"));
        }
    }

    private static BufferedReader openCalendarFile(String filename)
    {
        String fileExtension = filename.substring(filename.indexOf("."));
        String encoding = "UTF-8";
        if (fileExtension.equals(".utf16.cal"))
        {
            encoding = "UTF-16";
        }
        else if (fileExtension.equals(".utf32.cal"))
        {
            encoding = "UTF-32";
        }
        try 
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding));
            return br;
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
