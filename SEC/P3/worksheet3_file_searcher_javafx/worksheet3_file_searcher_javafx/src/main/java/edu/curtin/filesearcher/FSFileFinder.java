package edu.curtin.filesearcher;

import javafx.application.Platform;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.*;

public class FSFileFinder
{
    private String searchPath;
    private String searchTerm;
    private FSUserInterface ui;
    private Thread finderThread;
    private ExecutorService executor;

    public FSFileFinder(String searchPath, String searchTerm, FSUserInterface ui)
    {
        this.searchPath = searchPath;
        this.searchTerm = searchTerm;
        this.ui = ui;
        this.finderThread = new Thread(findFiles, "finderThread");
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    
    private Runnable findFiles = () ->
    {
        try
        {
            // Recurse through the directory tree
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    executor.submit(new FSFilter(file, searchTerm, ui));
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch(IOException e)
        {
            // This error handling is a bit quick-and-dirty, but it will suffice here.
            Platform.runLater(() ->
            {
                ui.showError(e.getClass().getName() + ": " + e.getMessage());
            });
        }
    };
 
    public void search()
    {
        finderThread.start();
    }
}
