/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.zip;

import java.io.File;

/**
 *
 * @author martin
 */
class ProgressContainer {

    private ProgressListener listener;
    private long lTotalBytes = 0;
    private long lTotalFiles = 0;
    private long lCurrentBytes = 0;
    private long lCurrentFiles = 0;

    public ProgressContainer(ProgressListener listener) {
        this.listener = listener;
    }
    
    public void init(File file_or_dir)
    {
        countFilesAndByte( file_or_dir );
        listener.init(lTotalBytes, lTotalFiles);
    }

    private void countFilesAndByte(File file_or_dir) {
        
        if( file_or_dir.isDirectory() )
        {
            File files[] =  file_or_dir.listFiles();

            for( File file : files )
            {
                countFilesAndByte( file );
            }
            return;
        }
        
        lTotalFiles++;
        lTotalBytes += file_or_dir.length();
    }
    
    public void incProgress( long lCountBytes, long lCountFiles )
    {
        lCurrentBytes += lCountBytes;
        lCurrentFiles += lCountFiles;
        
        listener.setProgress(lCurrentBytes, lCurrentFiles);
    }
}
