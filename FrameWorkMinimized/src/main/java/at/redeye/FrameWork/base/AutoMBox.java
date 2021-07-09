package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class AutoMBox {

    @FunctionalInterface
    public interface Doable {
        void do_stuff() throws Exception;
    }

    protected Logger logger;
    protected boolean failed;
    protected final boolean do_mbox;
    private final Doable doable;

    public AutoMBox(String loggerName, boolean do_mbox, Doable doable) {
        logger = LogManager.getLogger(loggerName);
        this.do_mbox = do_mbox;
        this.doable = doable;
        invoke();
    }

    public AutoMBox(String loggerName, Doable doable) {
        this(loggerName, true, doable);
    }

    private void invoke() {
        try {
            doable.do_stuff();
        } catch (Exception ex) {
            failed = true;
            logger.error("Exception: " + ex + "\n" + ex.getLocalizedMessage(), ex);
            if (do_mbox) {
                showErrorDialog(ex);
            }
        }
    }

    public boolean isFailed() {
        return failed;
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
}
