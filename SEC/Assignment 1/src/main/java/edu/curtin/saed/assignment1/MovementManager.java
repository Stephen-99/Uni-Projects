package edu.curtin.saed.assignment1;

import java.util.concurrent.*;

public class MovementManager 
{
    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> movementQueue;

    public MovementManager()
    {
        movementQueue = new LinkedBlockingQueue<>();

        /* using between a quarter and half the availible CPU threads so that there is still threads left over for 
         * The robots and various other separate threads. Otherwise the system could become over-saturated with 
         * threads and spend a lot of time context-switching. */
        int numCPUThreads = Runtime.getRuntime().availableProcessors();
        threadPool = new ThreadPoolExecutor(numCPUThreads / 4, numCPUThreads / 2, 10, TimeUnit.SECONDS, movementQueue);
        threadPool.prestartAllCoreThreads();
    }
    
    public void submitMovement(Movement task) throws InterruptedException
    {
        movementQueue.put(task);
    }

    public void shutdown()
    {
        threadPool.shutdown();
    }
}
