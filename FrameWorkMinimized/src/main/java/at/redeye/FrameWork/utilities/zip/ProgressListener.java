/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.zip;

/**
 *
 * @author martin
 */
public interface ProgressListener {
    
        public void init( long lTotalBytes, long lTotalFiles );
        public void setProgress(long lCountBytes, long lCountFiles );
    
}
