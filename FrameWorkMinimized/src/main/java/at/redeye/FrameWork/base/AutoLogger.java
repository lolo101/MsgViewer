package at.redeye.FrameWork.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.Callable;

public class AutoLogger<R> {

    private final Logger logger;
    private final Callable<R> callable;

    public AutoLogger(String loggerName, Invokable invokable) {
        this(loggerName, () -> {
            invokable.invoke();
            return null;
        });
    }

    public AutoLogger(String loggerName, Callable<R> callable) {
        logger = LogManager.getLogger(loggerName);
        this.callable = callable;
    }

    public R resultOrElse(R defaultValue) {
        try {
            return callable.call();
        } catch (Exception ex) {
            logger.error("Exception: " + ex, ex);
            return defaultValue;
        }
    }

    public Optional<R> result() {
        try {
            return Optional.ofNullable(callable.call());
        } catch (Exception ex) {
            logger.error("Exception: " + ex, ex);
            return Optional.empty();
        }
    }

    public void run() {
        resultOrElse(null);
    }
}
