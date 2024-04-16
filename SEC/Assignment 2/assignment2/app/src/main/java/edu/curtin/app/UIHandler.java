package edu.curtin.app;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.time.Period;

public class UIHandler
{   
    //If this is changed, make sure there is a corresponding properties file for the default locale
    public static final String DEFFAULT_LOCALE_STRING = "en-AU";

    private Calendar calendar;
    private ResourceBundle bundle;
    private Scanner sc;

    public UIHandler(Calendar calendar)
    {
        this.calendar = calendar;
        sc = new Scanner(System.in);
    }

    public Locale initUIHandler()
    {
        bundle = ResourceBundle.getBundle("edu.curtin.app.bundle", Locale.forLanguageTag(DEFFAULT_LOCALE_STRING));
        Locale locale = getLocale(sc, bundle);
        bundle = ResourceBundle.getBundle("edu.curtin.app.bundle", locale);
        
        return locale;
    }

    public void runUI()
    {
        calendar.showCalendar();
    
        String input = getInput(bundle.getString("menu_prompt"), sc, bundle);
        while (input.charAt(0) != 'q')
        {
            switch (input) {
                case "+d":
                    calendar.shiftCurrentDate(Period.ofDays(1));
                    break;
                case "+w":
                    calendar.shiftCurrentDate(Period.ofWeeks(1));
                    break;
                case "+m":
                    calendar.shiftCurrentDate(Period.ofMonths(1));
                    break;
                case "+y":
                    calendar.shiftCurrentDate(Period.ofYears(1));
                    break;
                case "-d":
                    calendar.shiftCurrentDate(Period.ofDays(-1));
                    break;
                case "-w":
                    calendar.shiftCurrentDate(Period.ofWeeks(-1));
                    break;
                case "-m":
                    calendar.shiftCurrentDate(Period.ofMonths(-1));
                    break;
                case "-y":
                    calendar.shiftCurrentDate(Period.ofYears(-1));
                    break;
                case "t":
                    calendar.resetCurrentDate();
                    break;
                case "s":
                    String searchTerm = getInput(bundle.getString("search_prompt"), sc, bundle);
                    calendar.searchForEvent(searchTerm);
                    break;
                
                default:
                    System.out.println(bundle.getString("menu_invalid_input"));
                    break;
            }

            input = getInput(bundle.getString("menu_prompt"), sc, bundle);
        }
        sc.close();
    }   

    private String getInput(String prompt, Scanner sc, ResourceBundle bundle)
    {
        System.out.println(prompt);
        String input = sc.nextLine();
        
        while (input.equals(""))
        {
            System.out.println(bundle.getString("empty_input"));
            System.out.println(prompt);
            input = sc.nextLine();
        }
        return input;
    }

    private Locale getLocale(Scanner sc, ResourceBundle bundle)
    {
        //In case the default is different on a different system
        Locale.setDefault(Locale.forLanguageTag(DEFFAULT_LOCALE_STRING));
        
        Locale locale;
        System.out.println(bundle.getString("locale_prompt"));
        String localeString = sc.nextLine();
        
        if(localeString.equals(""))
        {
            locale = Locale.getDefault();
        }
        else
        {
            locale = Locale.forLanguageTag(localeString);
        }

        return locale;
    }
}
