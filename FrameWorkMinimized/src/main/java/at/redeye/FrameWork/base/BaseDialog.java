package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBFlagInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;
import at.redeye.FrameWork.base.transaction.Transaction;
import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;
import at.redeye.SqlDBInterface.SqlDBIO.impl.TableBindingNotRegisteredException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.WrongBindFileFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Timer;

public class BaseDialog extends javax.swing.JFrame implements BindVarInterface,
		BaseDialogBase {

	private static final long serialVersionUID = 1L;

	public BaseDialogBaseHelper helper;
	JRootPane myrootPane;
	public static Logger logger = LogManager.getLogger(BaseDialog.class);
	public Root root;

	public BaseDialog(Root root, String title) {
		this.root = root;
		helper = new BaseDialogBaseHelper(this, root, title, myrootPane, false);
	}

	public BaseDialog(Root root, String title, boolean do_not_inform_root) {
		this.root = root;
		helper = new BaseDialogBaseHelper(this, root, title, myrootPane,
				do_not_inform_root);
	}

	/**
	 * Overload this method, if the window shouldn't open with with the last
	 * stored with and height.
	 *
	 * @return true if the size of the dialog should be stored
	 */
        @Override
	public boolean openWithLastWidthAndHeight() {
		return true;
	}

	/**
	 * automatically opens the Help Windows, when F1 is pressed
	 *
	 * @param runnable
	 *            This runnable should open the Help Window
	 */
	public void registerHelpWin(Runnable runnable) {
		helper.registerHelpWin(runnable);
	}

	/**
	 * opens the registerd Help win by Hand
	 */
	public void callHelpWin() {
		helper.callHelpWin();
	}

	/**
	 * Registers a listener for a F1, ESC, or somthing global keypressed Event
	 *
	 * @param to_listen_Key
	 *            Keyboard Key
	 * @param runnable
	 *            Method to call
	 */
        @Override
	public void registerActionKeyListener(KeyStroke to_listen_Key,
			Runnable runnable) {
		helper.registerActionKeyListener(to_listen_Key, runnable);
	}

	@Override
	protected JRootPane createRootPane() {
		myrootPane = super.createRootPane();

		return myrootPane;
	}

	/**
	 * @return The Transaction object for this dialog This Transaction object
	 *         will be automatically closed, on closing this this dialog. The
	 *         Transaction object will be only created once in the lifetime of
	 *         the dialog. So caching the Transaction object is not required.
	 *         <b>Can return null, in case of no database connection.</b>
	 */
        @Override
	public Transaction getTransaction() {
		return helper.getTransaction();
	}

	/**
	 * @return A new Transaction object, of the current database connection This
	 *         Transactino won't be closed on dialog closing event automatically
	 *         You have to close each allocated Transaction object yourself by
	 *         calling <b>closeTransaction()</b>
	 *
	 *         The Transaction object will by destroyed atomatically on
	 *         appliaction shutdown
	 */
        @Override
	public Transaction getNewTransaction() {
		return helper.getNewTransaction();
	}

	/**
	 * closes a given Transaction object. Rollback is done automatically.
	 *
	 * @param tran
	 *            a valid Transaction object
	 * @throws SQLException
	 *             if rollback fails
	 */
        @Override
	public void closeTransaction(Transaction tran) throws SQLException {
		helper.closeTransaction(tran);
	}

	/**
	 * closes the current dialog.
	 */
        @Override
	public void close() {
		helper.close();
	}

	/**
	 * Schließt das Fenster, ohne die Appliaktion zu beenden, auch wenn das zu
	 * schließende Fenster das Letzte offene ist. Das default Verhalten der
	 * Appliaktion ist, dass beim Schließen des letzten offenen Fensters die
	 * komplette Applikation geschlossen wird.
	 */
        @Override
	public void closeNoAppExit() {
		close();
	}

	/**
	 * Ermittelt den nächsten Wert für eine gegebene Sequenz
	 *
	 * @param seqName
	 * @return den nächsten Wert der Sequenz
	 * @throws java.sql.SQLException
	 * @throws at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException
	 * @throws WrongBindFileFormatException
	 * @throws TableBindingNotRegisteredException
	 * @throws IOException
	 */
        @Override
	public int getNewSequenceValue(String seqName) throws SQLException,
			UnsupportedDBDataTypeException, WrongBindFileFormatException,
			TableBindingNotRegisteredException, IOException {
		return helper.getNewSequenceValue(seqName);
	}

	/**
	 * @return 1 on Save Data <br/>
	 *         0 on Don't Save <br/>
	 *         -1 on Cancel <br/>
	 */
        @Override
	public int checkSave() {
		return helper.checkSave();
	}

	/**
	 * Checks, if data within the table have been change, asks the user what
	 * sould be done (save it, don't save it, or cancel current operation
	 *
	 * @param tm
	 *            TableManipulator object
	 * @return 1 when the data should by saved <br/>
	 *         0 on saving should be done <br/>
	 *         -1 cancel current operation <br/>
	 *
	 */
        @Override
	public int checkSave(TableManipulator tm) {
		return helper.checkSave(tm);
	}

        @Override
	public void doAutoRefresh() {
	}

	/**
	 * to be overrided by subdialogs
	 *
	 * @return true if the dialog can be closed
	 */
	@Override
	public boolean canClose() {
		return true;
	}

	@Override
	public void setEdited() {
		helper.setEdited();
	}

	@Override
	public boolean isEdited() {
		return helper.isEdited();
	}

	@Override
	public void setEdited(boolean val) {
		helper.setEdited(val);
	}

	@Override
	public void clearEdited() {
		helper.clearEdited();
	}

	/**
	 * Kontrolliert, ob in der übergebenen Tabelle nur ein Eintrag selektiert
	 * wurde. Wurde mehr als ein Eintrag selektiert, bekommt der User eine
	 * entsprechende Fehlermeldeung aufgeschalten und der Rückgabewert der
	 * Funktion ist false.
	 *
	 * @param table
	 *            eine jTable
	 * @return <b>true</b> Wenn nur ein Eintrag selektiert wurde und
	 *         <b>false</b>, wenn kein, oder mehrere Einträge selektiert wurden.
	 *         Eine ensprechende Fehlermeldung ist dabei dem User schon
	 *         aufgeschalten worden.
	 */
	public boolean checkAnyAndSingleSelection(JTable table) {
		return helper.checkAnyAndSingleSelection(table);
	}

	/**
	 * in jTextField an einen StringBuffer anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der StringBuffer
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextField jtext, StringBuffer var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * in jTextArea an eine StringBuffer anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der StringBuffer
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextArea jtext, StringBuffer var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * in jTextArea an eine DBValue anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der DBValue
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextArea jtext, DBValue var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * in jTextField an einen StringBuffer anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der StringBuffer
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	public void bindVar(JPasswordField jtext, StringBuffer var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * Ein jTextField an eine DBValue anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            die Datenbankvariable
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextField jtext, DBValue var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * Eine JCheckBox an eine DBFlagInteger Variable anbinden
	 *
	 * @param jtext
	 *            die Textbox
	 * @param var
	 *            die Datebanvariable
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	@Override
	public void bindVar(JCheckBox jtext, DBFlagInteger var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * Eine JComboBox an eine {@link DBValue} Variable anbinden
	 *
	 * @param jComboBox
	 *            die Combo-box
	 * @param var
	 *            die Datebanvariable
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	@Override
	public void bindVar(JComboBox jComboBox, DBValue var) {
		helper.bindVar(jComboBox, var);
	}

	/**
	 * Eine {@link IDateTimeComponent} an eine {@link DBDateTime} Variable
	 * anbinden
	 *
	 * @param comp
	 *            die DateTime Komponente
	 * @param dateTime
	 *            die Datebanvariable
	 *
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	@Override
	public void bindVar(IDateTimeComponent comp, DBDateTime dateTime) {
		helper.bindVar(comp, dateTime);
	}

	/**
	 * Alle Werte der angebunden Variablen in die entsprechenden GUI Komponenten
	 * übertragen
	 */
	@Override
	public void var_to_gui() {
		helper.var_to_gui();
	}

	/**
	 * Alle Elemnte des GUIs in die angebundenen Datenbankfelder kopieren
	 */
	@Override
	public void gui_to_var() {
		helper.gui_to_var();
	}

	/**
	 * gibt die <b>root</b> Klasse zurück
	 *
	 * @return
	 */
	public Root getRoot() {
		return helper.root;
	}

	/**
	 * Setzt den Sanduhr Mauscursor
	 */
        @Override
	public void setWaitCursor() {
		helper.setWaitCursor();
	}

	/**
	 * Setzt den Sanduhr, oder "normale" Mauscursor
	 *
	 * @param state
	 *            <b>true</b> für die Sanduhr und <b>false</b> für den nurmalen
	 *            Cursor
	 */
        @Override
	public void setWaitCursor(boolean state) {
		helper.setWaitCursor(state);
	}

	/**
	 * Setzt wieder den "normalen" Mauscursor
	 */
        @Override
	public void setNormalCursor() {
		helper.setWaitCursor(false);
	}

	/**
	 * Konfiguriert das jScrollpanel entsprechen dem im Setup hinterlegten
	 * Geschwindigkeit. Vom User über den Parameter VerticalScrollingSpeed
	 * einstellbar.
	 *
	 * @param scroll_panel
	 */
	@Override
	public void adjustScrollingSpeed(JScrollPane scroll_panel) {
		helper.adjustScrollingSpeed(scroll_panel);
	}

	/**
	 * Little helper function that sets the frame visible and push it to front,
	 * by useing the wait cursor.
	 *
	 * @param frame
	 */
	@Override
	public void invokeDialog(JFrame frame) {
		helper.invokeDialog(frame);
	}

	/**
	 * Little helper function that sets the frame visible and push it to front,
	 * by useing the wait cursor.
	 *
	 * @param dlg
	 */
	@Override
	public void invokeDialog(BaseDialogBase dlg) {
		helper.invokeDialog(dlg);
	}

	/**
	 * Little helper function that sets the frame visible and push it to front,
	 * by useing the wait cursor.
	 *
	 * @param dlg
	 */
	public void invokeDialog(BaseDialog dlg) {
		helper.invokeDialog((BaseDialogBase) dlg);
	}

	@Override
	public void invokeDialogUnique(BaseDialogBase dialog) {
		helper.invokeDialogUnique(dialog);
	}

	@Override
	public void registerOnCloseListener(Runnable runnable) {
		helper.registerOnCloseListener(runnable);
	}

	@Override
	public void deregisterOnCloseListener(Runnable runnable) {
		helper.deregisterOnCloseListener(runnable);
	}

	@Override
	public boolean closeSubdialogsOnClose() {
		return true;
	}

	@Override
	public Container getContainer() {
		return this;
	}

	public Window getWindow() {
		return this;
	}

	@Override
	public void invokeDialogModal(BaseDialogDialog dlg) {
		helper.invokeDialogModal(dlg);
	}

	@Override
	public void setBindVarsChanged(boolean state) {
		helper.setBindVarsChanged(state);
	}

	@Override
	public Collection<Pair> getBindVarPairs() {
		return helper.getBindVarPairs();
	}

	@Override
	public void addBindVarPair(Pair pair) {
		helper.addBindVarPair(pair);
	}

	@Override
	public void setBindVars(BindVarInterface bind_vars) {
		helper.setBindVars(bind_vars);
	}

	/**
	 * @param requester
	 *            The calling object (can be used to implement different
	 *            behavoir for eg saving size of window and table. Can be null
	 *
	 * @return a Dialog identifier for saving some data, eg: width an height of
	 *         the dialog. The default behavior is retuning the dialog title.
	 *         This function should be overloaded if some instances of dialogs
	 *         should all have the same eg size but it's no possible, because
	 *         each one has a different title.
	 */

	@Override
	public String getUniqueDialogIdentifier(Object requester) {
		return this.getClass().getName() + "/" + getTitle();
	}

	/**
	 * language the dialog is programmed in if not set, the settings from
	 * Root.getBaseLangague() are used
	 */
	@Override
	public void setBaseLanguage(String language) {
		helper.setBaseLanguage(language);
	}

	/**
	 * @return language the dialog is programmed in if not set, the settings
	 *         from Root.getBaseLangague() are used
	 */
	@Override
	public String getBaseLanguage() {
		return helper.getBaseLanguage();
	}

	@Override
	public void doLayout() {
		helper.doLayout();
		super.doLayout();
	}

	/**
	 * @param message
	 *            native langauge message
	 * @return translated message, if available
	 */
	@Override
	public String MlM(String message) {
		return helper.MlM(message);
	}

	@Override
	public void invokeMainDialog(BaseDialogBase dialog) {
		helper.invokeMainDialog(dialog);
	}

	@Override
	public Timer getAutoRefreshTimer() {
		return helper.getAutoRefreshTimer();
	}

	/**
	 * Ermittelt den nächsten Wert für eine gegebene Sequenz. Die <b>number</b>
	 * gibt dabei an wieviele Werte benötigt werden. Im Endeffekt darf dann der
	 * zurückgegeben Wert so oft, wie durch die Varible <b>number</> angeben
	 * erhöht werden.
	 *
	 * @param seqName
	 * @param number
	 *            die Anzahl der Werte die geliefert werden soll.
	 * @return den nächsten Wert der Sequenz
	 * @throws java.sql.SQLException
	 * @throws at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException
	 * @throws WrongBindFileFormatException
	 * @throws TableBindingNotRegisteredException
	 * @throws IOException
	 */
	@Override
	public int getNewSequenceValues(String seqName, int number)
			throws SQLException, UnsupportedDBDataTypeException,
			WrongBindFileFormatException, TableBindingNotRegisteredException,
			IOException {
		return helper.getNewSequenceValues(seqName, number);
	}
}
