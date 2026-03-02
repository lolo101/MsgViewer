package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class AutoMBox<R> {

    private final Logger logger;
    private final Callable<R> callable;

    public AutoMBox(String loggerName, Invokable invokable) {
        this(loggerName, () -> {
            invokable.invoke();
            return null;
        });
    }

    public AutoMBox(String loggerName, Callable<R> callable) {
        logger = LogManager.getLogger(loggerName);
        this.callable = callable;
    }

    private static void showErrorDialog(Exception ex) {
        Root root = Root.getLastRoot();

        JOptionPane.showMessageDialog(null,
                StringUtils.autoLineBreak(
                        root.MlM("Es ist ein Fehler aufgetreten:") + " "
                                + ex.getLocalizedMessage()),
                root.MlM("Error"),
                JOptionPane.ERROR_MESSAGE);
    }

    public R resultOrElse(R defaultValue) {
        try {
            return callable.call();
        } catch (Exception ex) {
            logger.error("Exception: " + ex, ex);
            showErrorDialog(ex);
            return defaultValue;
        }
    }

    public void onSuccess(Consumer<R> consumer) {
        try {
            consumer.accept(callable.call());
        } catch (Exception ex) {
            logger.error("Exception: " + ex, ex);
            showErrorDialog(ex);
        }
    }

    public void run() {
        resultOrElse(null);
    }
}
