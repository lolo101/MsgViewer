package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.transaction.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;

public interface BaseDialogBase
{
    void close();
    boolean canClose();

    void setTitle(String title);

    String getTitle();

    /**
     * @param requester The calling object (can be used to implement different behavoir for eg
     *        saving size of window and table. Can be null
     *
     * @return a Dialog identifier for saving some data, eg:
     * width an height of the dialog.
     * The default behavior is retuning the dialog title.
     * This function should be overloaded if some instances of dialogs
     * should all have the same eg size but it's no possible, because
     * each one has a different title.
     */

    String getUniqueDialogIdentifier(Object requester);

    void addWindowListener(WindowListener windowListener);

    int getWidth();

    int getHeight();

    void setBounds(int x, int y, int i, int i0);

    boolean openWithLastWidthAndHeight();

    void setPreferredSize(Dimension dimension);

    /**
     * Registers a listener for a F1, ESC, or something global keypressed Event
     * @param keyStroke Keyboard Key
     * @param runnable      Method to call
     */
    void registerActionKeyListener(KeyStroke keyStroke, Runnable runnable);

    Container getContainer();

    void setCursor(Cursor predefinedCursor);

    void adjustScrollingSpeed(JScrollPane scroll_panel);

    void setVisible(boolean b);

    void toFront();

    void invokeDialog(JFrame frame);

    void invokeDialog(BaseDialogBase dlg);

    void invokeDialogUnique(BaseDialogBase dialog);

    /**
     * opens the dialog as a new main Dialog. So if the source Dialog
     * is closed, the subdialog won't be closed, because it's a new main dialog.
     */
    void invokeMainDialog(BaseDialogBase dialog);

    void registerOnCloseListener(Runnable runnable);

    boolean closeSubdialogsOnClose();

    void setEdited();

    void setEdited(boolean val);

    /**
     * @return The Transaction object for this dialog
     * This Transaction object will be automatically closed, on closing this this
     * dialog. The Transaction object will be only created once in the lifetime
     * of the dialog. So caching the Transaction object is not required.
     * <b>Can return null, in case of no database connection.</b>
     */
    Transaction getTransaction();

    void dispose();

    int getX();

    int getY();

    void closeNoAppExit();

    /**
    * language the dialog is programmed in
    * if not set, the settings from Root.getBaseLangague() are used
    */
    void setBaseLanguage(String language);

    /**
    * @return language the dialog is programmed in
    * if not set, the settings from Root.getBaseLangague() are used
    */
    String getBaseLanguage();

    /**
     * @param message native langauge message
     * @return translated message, if available
     */
    String MlM(String message);

    void setWaitCursor();

    void setNormalCursor();
}
