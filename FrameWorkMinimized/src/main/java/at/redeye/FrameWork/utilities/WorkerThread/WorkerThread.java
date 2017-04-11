/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.WorkerThread;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 *
 * @author martin
 */
public class WorkerThread extends Thread
{               
    private ConcurrentLinkedQueue<WorkInterface> queue_to_work = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<WorkInterface> queue_done = new ConcurrentLinkedQueue();
    private boolean should_stop = false;
    private boolean current_working = false;
    
    /**
     * current working package
     */
    WorkInterface work = null;
    
    public WorkerThread( String name ) {
        super( name );
    }

    public WorkerThread() {}

    
    
    @Override
    public void run()
    {
        do
        {                        
            while( ( work = queue_to_work.poll() ) != null && !should_stop)
            {
                current_working = true;
                work.work();
                queue_done.add(work);
                current_working = false;
            }
    
            if( work == null )
                idle();
            
        } while( !should_stop );
    }
    
    /**
     * called when idle
     */
    public void idle()
    {
        try {
            sleep(300);
        } catch( InterruptedException ex ) {
            
        }
    }
    
    public void stopWorking()
    {
        should_stop = true;   
                
        if (work == null) {
            return;
        }

        work.pleaseStopWorking();
    }
    
    /**
     * call this function when you want that all the finnish function
     * of the worker is done
     */
    public void callFinishedWork()
    {
        WorkInterface work = null;
        while ((work = queue_done.poll()) != null && !should_stop) {
            work.workDone();
        }
    }
    
    public void add( WorkInterface work )
    {
        queue_to_work.add(work);
    }
    
    public boolean isIdle()
    {
        if( queue_to_work.isEmpty() && queue_done.isEmpty() && !current_working )
            return true;
        
        return false;
    }
}
