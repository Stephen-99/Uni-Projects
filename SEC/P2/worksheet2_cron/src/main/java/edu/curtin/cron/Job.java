package edu.curtin.cron;
import java.io.*;

/**
 * Represents a job (command) to be run, and performs the execution. (You need to fill in the 
 * details!)
 */
public class Job implements Runnable
{
    private String command;
    private int delay;
    private Logger logger;

    public Job(String cmd, int delay, Logger logger) 
    {
        command = cmd;
        this.delay = delay;
        this.logger = logger;

    }

    public int getDelay()
    {
        return delay;
    }

    @Override
    public void run() 
    {
        try
        {
            // Assume 'command' is a string containing the command the
            // execute. Then we initially run it as follows:
            Process proc = Runtime.getRuntime().exec(command);

            // Arrange to capture the command's output, line by line.
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
            String line = reader.readLine();
            while(line != null)
            {
                output.append(line);
                output.append('\n');
                line = reader.readLine();
            }
            // We've now reached the end of the command's output, which
            // generally means the command has finished.
            logger.logMessage(command + ": " + output.toString());
        }
        catch (IOException e)
        {
            System.out.println("There was an IOException when running the command: " + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.out.println("The thread for the job " + command + " was interrupted");
        }
    }
}
