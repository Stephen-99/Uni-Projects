package edu.curtin.calplugins;

import edu.curtin.api.*;
import javax.swing.JOptionPane;
import java.text.Normalizer;
import java.util.Dictionary;

public class Notify implements CalendarPlugin
{
    private String searchText;

    public Notify(Dictionary<String, String> args)
    {
        this.searchText = Normalizer.normalize(args.get("text").toLowerCase(), Normalizer.Form.NFKC);
    }

    @Override
    public void start(CalendarAPI api) 
    {
        api.registerEventHandler(new NotificationHandler());
    }
    
    private class NotificationHandler implements CalendarEventHandler
    {
        @Override
        public void eventStarted(CalendarEvent event) 
        {
            String normalizedEventTitle = Normalizer.normalize(event.getTitle().toLowerCase(), Normalizer.Form.NFKC);
            if (normalizedEventTitle.contains(searchText))
            {
                JOptionPane.showMessageDialog(null, "The following event has started.\n" + event.getDisplayText());
            }
        }
        
    }
}
