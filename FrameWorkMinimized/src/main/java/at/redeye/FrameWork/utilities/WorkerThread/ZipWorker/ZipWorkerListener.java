/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.WorkerThread.ZipWorker;

import java.io.File;

/**
 *
 * @author martin
 */
public interface ZipWorkerListener 
{
    /**
     * called before starting compression
     * @param directory or file that should be compressed
     * @param lTotalBytes
     * @param lTotalFiles 
     */
    void initZipProgress(File file_or_dir, long lTotalBytes, long lTotalFiles);
    
    /**
     * periodical called for feeding a progress bar
     * @param dir
     * @param lCurrentBytes
     * @param lCurrentFiles
     * @param started_at time in millis when we started compressing the file
     */
    void setZipProgress(File file_or_dir, long lCurrentBytes, long lCurrentFiles, long started_at);          
     
    /**
     * called when the zipping has been finnished
     * @param dir
     * @param tempfile the target filename. Created in tempdir. You are responsible
     * for cleaning up this file.
     */
    void compressingDone(File file_or_dir, File tempfile);
    
    /**
     * called when an error appeared
     * @param dir
     * @param error 
     */
    void failedCompressing(File dir, Exception error);
}
