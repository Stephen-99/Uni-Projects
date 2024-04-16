package edu.curtin.cron;
import java.util.*;

/**
 * The scheduler keeps track of all the jobs, and runs each one at the appropriate time. (You need
 * to fill in the details!)
 */
public class Scheduler
{
    private Thread schedulerThread;
    private Object mutex = new Object();
    private LinkedList<Job> jobs = new LinkedList<>(); 

    public Scheduler()
    {
        schedulerThread = new Thread(schedulingTask, "schedulerThread");
    }
    
    Runnable schedulingTask = () ->
    {
        try 
        {
            long timeWaiting = 0;
            while (true)
            {
                synchronized(mutex)
                {
                    for (Job job : jobs)
                    {
                        if (timeWaiting % job.getDelay() == 0)
                        {
                            Thread jobThread = new Thread(job, "jobThread");
                            jobThread.start();
                        }
                    }
                }
                Thread.sleep(1000L);
                timeWaiting += 1;
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Scheduler thread was interrupted.");
        }
    };

    public void addJob(Job newJob)
    {
        synchronized(mutex)
        {
            jobs.addLast(newJob);
        }
    }
    
    public void start()
    {
        schedulerThread.start();
    }

    public void stop()
    {
        schedulerThread.interrupt();
    }
}
