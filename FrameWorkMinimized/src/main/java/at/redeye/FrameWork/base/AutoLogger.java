/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author martin
 */
public abstract class AutoLogger {

    protected Logger logger;
    protected Exception thrown_ex = null;
    private boolean failed = true;
    protected boolean logical_failure = false;
    public Object result = null;

    public AutoLogger( String className )
    {
        logger = LogManager.getLogger(className);

        invoke();
    }

    private void invoke()
    {
        try {
            do_stuff();
            failed = false;
        } catch ( Exception ex ) {
            logger.error("Exception: " + ex, ex);
            thrown_ex = ex;
        }
    }

    public abstract void do_stuff() throws Exception;

    public boolean isFailed()
    {
        return failed || logical_failure;
    }

    protected void setFailed()
    {
        logical_failure = true;
    }

    protected void clearFailed()
    {
        logical_failure = false;
    }
}
