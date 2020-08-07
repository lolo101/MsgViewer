/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public abstract class AutoMBox
{
    public interface ShowAdvancedException
    {
        /**
         * @param ex
         * @return false, if the default exceptiondialog should be shown
         */
        boolean wantShowAdvancedException( Exception ex );
        void showAdvancedException( Exception ex );
    }

    protected Logger logger;
    protected Exception thrown_ex = null;
    protected boolean failed = true;
    protected final boolean do_mbox;
    public boolean logical_failure = false;

    protected static ArrayList<ShowAdvancedException> show_exception_handlers = null;

    public AutoMBox( String className, boolean do_mbox )
    {
        logger = LogManager.getLogger(className);
        this.do_mbox = do_mbox;
        invoke();
    }

    public AutoMBox( String className )
    {
        this(className, true);
    }

    public static void addShowAdvancedExceptionHandle( ShowAdvancedException handler )
    {
        if( show_exception_handlers == null )
            show_exception_handlers = new ArrayList<>();

        show_exception_handlers.add(handler);
    }

    private void invoke()
    {
        try {
            do_stuff();
            failed = false;
        } catch (Exception ex) {
            logger.error("Exception: " + ex + "\n" + ex.getLocalizedMessage(), ex );
            thrown_ex = ex;
        }

        if (thrown_ex != null) {
            if (do_mbox) {

                boolean show_default_dialog = true;

                if( show_exception_handlers != null )
                {
                    for( ShowAdvancedException handler : show_exception_handlers )
                    {
                        if( handler.wantShowAdvancedException(thrown_ex) ) {
                            show_default_dialog = false;
                            handler.showAdvancedException(thrown_ex);
                        }
                    }
                }


                if (show_default_dialog) {
                    Root root = Root.getLastRoot();

                    JOptionPane.showMessageDialog(null,
                            StringUtils.autoLineBreak(
                            root.MlM("Es ist ein Fehler aufgetreten:") + " "
                            + thrown_ex.getLocalizedMessage()),
                            root.MlM("Error"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public abstract void do_stuff() throws Exception;

    public boolean isFailed()
    {
        return failed || logical_failure;
    }
}
