package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class AutoMBox {

    protected Logger logger;
    protected boolean failed;
    protected final boolean do_mbox;

    public AutoMBox(String loggerName, boolean do_mbox, Invokable invokable) {
        logger = LogManager.getLogger(loggerName);
        this.do_mbox = do_mbox;
        invoke(invokable);
    }

    public AutoMBox(String loggerName, Invokable invokable) {
        this(loggerName, true, invokable);
    }

    private void invoke(Invokable invokable) {
        try {
            invokable.invoke();
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
