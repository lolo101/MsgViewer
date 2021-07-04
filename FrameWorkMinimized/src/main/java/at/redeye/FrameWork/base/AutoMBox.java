package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;

public class AutoMBox {
    public interface ShowAdvancedException {
        /**
         * @return false, if the default exceptiondialog should be shown
         */
        boolean wantShowAdvancedException(Exception ex);

        void showAdvancedException(Exception ex);
    }

    @FunctionalInterface
    public interface Doable {
        void do_stuff() throws Exception;
    }

    protected Logger logger;
    protected boolean failed = true;
    protected final boolean do_mbox;
    private final Doable doable;

    protected static ArrayList<ShowAdvancedException> show_exception_handlers = null;

    public AutoMBox(String className, boolean do_mbox, Doable doable) {
        logger = LogManager.getLogger(className);
        this.do_mbox = do_mbox;
        this.doable = doable;
        invoke();
    }

    public AutoMBox(String className, Doable doable) {
        this(className, true, doable);
    }

    private void invoke() {
        try {
            doable.do_stuff();
            failed = false;
        } catch (Exception ex) {
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
        boolean show_default_dialog = true;

        if (show_exception_handlers != null) {
            for (ShowAdvancedException handler : show_exception_handlers) {
                if (handler.wantShowAdvancedException(ex)) {
                    show_default_dialog = false;
                    handler.showAdvancedException(ex);
                }
            }
        }

        if (show_default_dialog) {
            Root root = Root.getLastRoot();

            JOptionPane.showMessageDialog(null,
                    StringUtils.autoLineBreak(
                            root.MlM("Es ist ein Fehler aufgetreten:") + " "
                                    + ex.getLocalizedMessage()),
                    root.MlM("Error"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
