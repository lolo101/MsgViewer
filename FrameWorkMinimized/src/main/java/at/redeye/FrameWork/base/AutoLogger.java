package at.redeye.FrameWork.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoLogger {

    private final Logger logger;
    private boolean failed;

    public AutoLogger(String className, Invokable invokable)
    {
        logger = LogManager.getLogger(className);

        invoke(invokable);
    }

    private void invoke(Invokable invokable)
    {
        try {
            invokable.invoke();
        } catch ( Exception ex ) {
            failed = true;
            logger.error("Exception: " + ex, ex);
        }
    }

    public boolean isFailed()
    {
        return failed;
    }

}
