/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.WorkerThread;

/**
 *
 * @author martin
 */
public class OperationCanceledException extends RuntimeException
{
   public OperationCanceledException(String message) {
        super(message);
   }
   
   public OperationCanceledException() {
        super("operation canceled");
   }
}
