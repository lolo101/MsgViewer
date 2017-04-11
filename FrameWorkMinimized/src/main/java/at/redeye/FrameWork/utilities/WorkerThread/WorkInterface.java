/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.WorkerThread;

/**
 *
 * @author martin
 */
public interface WorkInterface 
{
    /**
     * function called by the worker thread, where all the stuff should be done
     */
    public void work();
    
    /**
     * function called in java.awt.eventQueue Thread when the working is finished
     */
    public void workDone();
    
    
    /**
     * called from the WorkerThread when from the working thread itself stopWorking() is called
     */
    public void pleaseStopWorking();
}
