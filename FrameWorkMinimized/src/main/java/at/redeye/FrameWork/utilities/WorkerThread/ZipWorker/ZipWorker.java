/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities.WorkerThread.ZipWorker;

import at.redeye.FrameWork.utilities.WorkerThread.OperationCanceledException;
import at.redeye.FrameWork.utilities.WorkerThread.WorkInterface;
import at.redeye.FrameWork.utilities.WorkerThread.WorkerThread;
import at.redeye.FrameWork.utilities.zip.ProgressListener;
import at.redeye.FrameWork.utilities.zip.Zip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class ZipWorker
{
    private static final Logger logger = LogManager.getLogger(ZipWorker.class);

    ZipWorkerListener parent;
    WorkerThread worker_thread;

    class ZipDirectory implements WorkInterface, ProgressListener
    {
        File dir;
        Exception error;
        File tempfile;
        private long started_at = 0;
        private long lastInfo = 0;
        boolean should_stop = false;

        public ZipDirectory( File dir )
        {
            this.dir = dir;
        }

        @Override
        public void work() {
            try
            {
                tempfile = File.createTempFile("temp", ".zip");
                Zip.zip(dir, tempfile, this);

            } catch( IOException ex ) {
                error = ex;
                logger.error("Failed zipping directory " + dir.getPath(), ex );
                if( tempfile != null ) {
                    if( !tempfile.delete() )
                        logger.error("failed deleting temp file " + tempfile.getPath());
                }
            }

        }

        @Override
        public void workDone() {

            if( error != null )
            {
                parent.failedCompressing(dir,error);
            }
            else
            {
                parent.compressingDone(dir,tempfile);
            }
        }

        @Override
        public void init(final long lTotalBytes, final long lTotalFiles) {

            started_at = System.currentTimeMillis();

            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    parent.initZipProgress( dir, lTotalBytes, lTotalFiles);
                }
            });
        }

        @Override
        public void setProgress(final long lCountBytes, final long lCountFiles) {

            if( should_stop )
                throw new OperationCanceledException();

            long now = System.currentTimeMillis();

            if( now - lastInfo < 300 )
                return;

            lastInfo = now;

               java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    parent.setZipProgress( dir, lCountBytes, lCountFiles,started_at);
                }
            });
        }

        @Override
        public void pleaseStopWorking() {
            should_stop = true;
        }
    }

    public ZipWorker( ZipWorkerListener parent )
    {
        this.parent = parent;
    }

    public void onTimeout()
    {
        if( worker_thread == null )
            return;

        worker_thread.callFinishedWork();
    }

    public void compressDir( File directory )
    {
        if( worker_thread == null ) {
            worker_thread = new WorkerThread(this.getClass().getName());
            worker_thread.start();
        }

        worker_thread.add(new ZipDirectory(directory));
    }

    public boolean isIdle() {
        if( worker_thread == null )
            return true;

        return worker_thread.isIdle();
    }
}
