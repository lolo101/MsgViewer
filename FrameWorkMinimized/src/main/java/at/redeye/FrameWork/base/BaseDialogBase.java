package at.redeye.FrameWork.base;

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
     * @return a Dialog identifier for saving some data, eg:
     * width and height of the dialog.
     * The default behavior is retuning the dialog title.
     * This function should be overloaded if some instances of dialogs
     * should all have the same e.g. size, but it's not possible, because
     * each one has a different title.
     */

    String getUniqueDialogIdentifier();

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

    void invokeDialogUnique(BaseDialogBase dialog);

    void registerOnCloseListener(Runnable runnable);

    boolean closeSubdialogsOnClose();

    void setEdited();

    void setEdited(boolean val);

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
     * @param message native langauge message
     * @return translated message, if available
     */
    String MlM(String message);

    void setWaitCursor();

    void setNormalCursor();
}
