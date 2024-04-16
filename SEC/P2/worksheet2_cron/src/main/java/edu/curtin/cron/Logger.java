package edu.curtin.cron;
import java.io.*;

/**
 * The logger is in charge of writing output to 'cron.log'. It does this in its own thread, but 
 * assumes that other threads will call the setMessage() in order to provide messages to log. (You 
 * need to fill in the details!)
 */
public class Logger
{
    private String nextMessage;
    private Thread loggerThread;
    private Object monitor;

    public Logger()
    {
        loggerThread = new Thread(loggerTask, "loggerThread");
        monitor = new Object();
    }
    
    Runnable loggerTask = () ->
    {
        try 
        {
            while (true)
            {
                synchronized(monitor)
                {
                    monitor.wait();
                    try(PrintWriter writer =
                        new PrintWriter(new FileWriter("cron.log", true)))
                    {
                        writer.println(nextMessage);
                        nextMessage = null;
                        monitor.notify();
                    }
                    catch(IOException e)
                    {
                        nextMessage = null;
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Logger thread was interrupted.");
        }
    };

    public void logMessage(String newMessage) throws InterruptedException
    {   
        synchronized(monitor)
        {
            if (nextMessage != null)
            {
                monitor.wait();
            }
            nextMessage = newMessage;
            monitor.notify();
        }
    }

    public void start()
    {
        loggerThread.start();
    }
    
    public void stop()
    {
        loggerThread.interrupt();
    }
}
