package edu.curtin.filesearcher;

import javafx.application.Platform;
import java.util.concurrent.*;
import org.apache.commons.io.*;
import java.io.*;
import java.nio.file.*;

//This class will become redundant
    //Could otherwise put the executor service and the filtering alg (Runnable) in here instead.
public class FSFilter implements Callable<Void>
{
    private Path file;
    private String searchTerm;
    private FSUserInterface ui;

    public FSFilter(Path file, String searchTerm, FSUserInterface ui)
    {
        this.file = file;
        this.searchTerm = searchTerm;
        this.ui = ui;
    }

    @Override
    public Void call() throws Exception
    {
        try
        {
            //Get all the contents from the file. Does it contain a match?
            if (FileUtils.readFileToString(file.toFile()).contains(searchTerm))
            {
                Platform.runLater(() ->
                {
                    ui.addResult(file.toString());
                });
            }
        }
        catch (IOException e)
        {
            // This error handling is a bit quick-and-dirty, but it will suffice here.
            Platform.runLater(() ->
            {
                ui.showError(e.getClass().getName() + ": " + e.getMessage() + "    FOUND in file: " + file.toString());
            });
        }
        return null;
    }
}
