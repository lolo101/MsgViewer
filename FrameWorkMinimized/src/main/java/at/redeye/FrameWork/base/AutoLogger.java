package at.redeye.FrameWork.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

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

    public void onSuccess(Consumer<R> consumer) {
        try {
            consumer.accept(callable.call());
        } catch (Exception ex) {
            logger.error("Exception: " + ex, ex);
        }
    }

    public void run() {
        resultOrElse(null);
    }
}
