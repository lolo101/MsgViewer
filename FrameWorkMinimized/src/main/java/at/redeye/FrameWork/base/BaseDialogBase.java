/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;
import at.redeye.FrameWork.base.transaction.Transaction;
import at.redeye.SqlDBInterface.SqlDBIO.impl.TableBindingNotRegisteredException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.WrongBindFileFormatException;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 *
 * @author martin
 */
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
     * Registers a listener for a F1, ESC, or somthing global keypressed Event
     * @param to_listen_Key Keyboard Key
     * @param runnable      Method to call
     */
    void registerActionKeyListener(KeyStroke keyStroke, Runnable runnable);

    Container getContainer();

    void doAutoRefresh();

    void setCursor(Cursor predefinedCursor);

    void adjustScrollingSpeed(JScrollPane scroll_panel);

    void setVisible(boolean b);

    void toFront();

    void invokeDialog(JFrame frame);

    void invokeDialog(BaseDialogBase dlg);

    void invokeDialogModal(BaseDialogDialog dlg);

    void invokeDialogUnique(BaseDialogBase dialog);

    /**
     * opens the dialog as a new main Dialog. So if the source Dialog
     * is closed, the subdialog won't be closed, because it's a new main dialog.
     * @param dialog
     */
    void invokeMainDialog(BaseDialogBase dialog);

    void registerOnCloseListener(Runnable runnable);

    void deregisterOnCloseListener(Runnable runnable);

    boolean closeSubdialogsOnClose();

    void setEdited();

    boolean isEdited();

    void setEdited(boolean val);

    void clearEdited();

    /**
     * Checks, if data within the table have been change, asks the
     * user what sould be done (save it, don't save it, or cancel current operation
     * @param tm TableManipulator object
     * @return
     *   1 when the data should by saved <br/>
     *   0 on saving should be done <br/>
     *  -1 cancel current operation <br/>
     *
     */
    int checkSave(TableManipulator tm);

    /**
     * @return 1 on Save Data  <br/>
     *         0 on Don't Save <br/>
     *        -1 on Cancel <br/>
     */
    int checkSave();

    /**
     * @return The Transaction object for this dialog
     * This Transaction object will be automatically closed, on closing this this
     * dialog. The Transaction object will be only created once in the lifetime
     * of the dialog. So caching the Transaction object is not required.
     * <b>Can return null, in case of no database connection.</b>
     */
    Transaction getTransaction();

    /**
     * @return A new Transaction object, of the current database connection
     * This Transactino won't be closed on dialog closing event automatically
     * You have to close each allocated Transaction object yourself by calling
     * <b>closeTransaction()</b>
     *
     * The Transaction object will by destroyed atomatically on appliaction shutdown
     */
    Transaction getNewTransaction();

    /**
     * closes a given Transaction object. Rollback is done automatically.
     * @param tran a valid Transaction object
     * @throws SQLException if rollback fails
     */
    void closeTransaction(Transaction tran) throws SQLException;

/**
     * Ermittelt den nächsten Wert für eine gegebene Sequenz
     * @param seqName
     * @return den nächsten Wert der Sequenz
     * @throws java.sql.SQLException
     * @throws at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException
     * @throws WrongBindFileFormatException
     * @throws TableBindingNotRegisteredException
     * @throws IOException
     */
int getNewSequenceValue(String seqName) throws SQLException, UnsupportedDBDataTypeException, WrongBindFileFormatException, TableBindingNotRegisteredException, IOException;

/**
     * Ermittelt den nächsten Wert für eine gegebene Sequenz. Die <b>number</b> gibt dabei an wieviele
     * Werte benötigt werden. Im Endeffekt darf dann der zurückgegeben Wert so oft, wie durch die Varible <b>number</> angeben
     * erhöht werden.
     * @param seqName
     * @param number die Anzahl der Werte die geliefert werden soll.
     * @return den nächsten Wert der Sequenz
     * @throws java.sql.SQLException
     * @throws at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException
     * @throws WrongBindFileFormatException
     * @throws TableBindingNotRegisteredException
     * @throws IOException
     */
int getNewSequenceValues(String seqName, int number) throws SQLException, UnsupportedDBDataTypeException, WrongBindFileFormatException, TableBindingNotRegisteredException, IOException;

    void dispose();

    int getX();

    int getY();

    void closeNoAppExit();

    void setBindVarsChanged(boolean state);

    void setBindVars(BindVarInterface bind_vars);

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

    Timer getAutoRefreshTimer();


    /**
     * Returns the minimum size of this container.  If the minimum size has
     * not been set explicitly by {@link Component#setMinimumSize(Dimension)}
     * and this {@code Container} has a {@code non-null} {@link LayoutManager},
     * then {@link LayoutManager#minimumLayoutSize(Container)}
     * is used to calculate the minimum size.
     *
     * <p>Note: some implementations may cache the value returned from the
     * {@code LayoutManager}.  Implementations that cache need not invoke
     * {@code minimumLayoutSize} on the {@code LayoutManager} every time
     * this method is invoked, rather the {@code LayoutManager} will only
     * be queried after the {@code Container} becomes invalid.
     *
     * @return    an instance of <code>Dimension</code> that represents
     *                the minimum size of this container.
     * @see       #getPreferredSize
     * @see       #getMaximumSize
     * @see       #getLayout
     * @see       LayoutManager#minimumLayoutSize(Container)
     * @see       Component#getMinimumSize
     * @since     JDK1.1
     */
    Dimension getMinimumSize();

    void setWaitCursor();

    void setWaitCursor(boolean state);

    void setNormalCursor();
}
