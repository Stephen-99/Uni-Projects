package edu.curtin.app;

import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import edu.curtin.terminalgrid.TerminalGrid;

public class CalendarGrid 
{
    @SuppressWarnings("PMD") //Capitals are convention for constants
    public final int NUM_DAYS_TO_DISPLAY = 7;
    private List<List<String>> listMessages;
    private List<String> rowHeadings;
    private List<String> colHeadings;
    private Locale locale;

    public CalendarGrid(CurrentCalInfo calInfo, Locale locale)
    {
        this.locale = locale;
        resetRows(calInfo);
    }
 
    private void addRow(String[] row, String rowHeading)
    {
        if (row.length != NUM_DAYS_TO_DISPLAY)
        {
            return;
        }
        
        listMessages.add(Arrays.asList(row));
        rowHeadings.add(rowHeading);
    }

    private void addRows(CurrentCalInfo calInfo)
    {
        for (CurrentCalInfo.CalRow row : calInfo) 
        {
            addRow(row.getRow(), row.getRowHeading());
        }
    }

    public final void resetRows(CurrentCalInfo calInfo)
    {
        colHeadings = new ArrayList<>();
        for (int ii = 0; ii < 7; ii++)
        {
            colHeadings.add(calInfo.getCurrentDate().plusDays(ii).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)));
        }
        
        rowHeadings = new ArrayList<>();
        listMessages = new ArrayList<>();
        addRows(calInfo);
    }

    public void displayGrid(ResourceBundle bundle)
    {
        //If it's empty, print the dates still, and let the user know
        if (listMessages.isEmpty())
        {
            listMessages.add(Arrays.asList("", "", "", "", "", "", ""));
            rowHeadings.add(bundle.getString("no_events"));
        }

        var terminalGrid = new TerminalGrid(System.out, 120);

        // using plain ASCII characters since on my machine the others weren't working properly.
        terminalGrid.setTerminalWidth(145);
        terminalGrid.setBoxChars(TerminalGrid.ASCII_BOX_CHARS);
        terminalGrid.print(listMessages, rowHeadings, colHeadings);
        System.out.println();
    }
}
