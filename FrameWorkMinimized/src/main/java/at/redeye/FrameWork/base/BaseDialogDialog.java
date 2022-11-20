package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBFlagInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class BaseDialogDialog extends javax.swing.JDialog implements
		BindVarInterface, BaseDialogBase {

	private static final long serialVersionUID = 1L;
	protected static final Logger logger = LogManager.getLogger(BaseDialogDialog.class);

	protected final Root root;
	private final BaseDialogBaseHelper helper;

	public BaseDialogDialog(Root root, String title) {
		this(null, root, title, false);
	}

	public BaseDialogDialog(JFrame owner, Root root, String title) {
		this(owner, root, title, false);
	}

	public BaseDialogDialog(JFrame owner, Root root, String title,
			boolean do_not_inform_root) {
		super(owner);
		this.root = root;

		helper = new BaseDialogBaseHelper(this, root, title, null,
				do_not_inform_root);
	}

	/**
	 * Overload this method, if the window shouldn't open with the last
	 * stored with and height.
	 *
	 * @return true if the size of the dialog should be stored
	 */
	public boolean openWithLastWidthAndHeight() {
		return !Setup.is_win_system();
	}

	/**
	 * Registers a listener for a F1, ESC, or something global keypressed Event
	 *
	 * @param to_listen_Key
	 *            Keyboard Key
	 * @param runnable
	 *            Method to call
	 */
	public void registerActionKeyListener(KeyStroke to_listen_Key,
			Runnable runnable) {
		helper.registerActionKeyListener(to_listen_Key, runnable);
	}

	@Override
	protected JRootPane createRootPane() {
		return super.createRootPane();
	}

	/**
	 * closes the current dialog.
	 */
	public void close() {
		helper.close();
	}

	/**
	 * Schließt das Fenster, ohne die Appliaktion zu beenden, auch wenn das zu
	 * schließende Fenster das Letzte offene ist. Das default Verhalten der
	 * Appliaktion ist, dass beim Schließen des letzten offenen Fensters die
	 * komplette Applikation geschlossen wird.
	 */
	public void closeNoAppExit() {
		close();
	}

	/**
	 * to be overrided by subdialogs
	 *
	 * @return true if the dialog can be closed
	 */
	public boolean canClose() {
		return true;
	}

	public void setEdited() {
		helper.setEdited();
	}

	public void setEdited(boolean val) {
		helper.setEdited(val);
	}

	/**
	 * in jTextField an einen StringBuffer anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der StringBuffer
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	public void bindVar(JTextField jtext, StringBuffer var) {
		helper.bindVar(jtext, var);
	}

	/**
	 * Ein jTextField an eine DBValue anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            die Datenbankvariable
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
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
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	@Override
	public void bindVar(JComboBox<?> jComboBox, DBValue var) {
		helper.bindVar(jComboBox, var);
	}

	/**
	 * Eine {@link IDateTimeComponent} an eine {@link DBDateTime} Variable
	 * anbinden
	 *
	 * @param comp
	 *            die DateTime-Komponente
	 * @param dateTime
	 *            die Datebanvariable
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
	@Override
	public void bindVar(IDateTimeComponent comp, DBDateTime dateTime) {
		helper.bindVar(comp, dateTime);
	}

	/**
	 * in jTextArea an eine StringBuffer anbinden
	 *
	 * @param jtext
	 *            das Textfeld
	 * @param var
	 *            der StringBuffer
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
	 *            Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
	 *            dann der demenstprechende Inhalt entweder vom GUI zu
	 *            Variablen, oder umgekehrt übertragen.
	 */
        @Override
	public void bindVar(JTextArea jtext, DBValue var) {
		helper.bindVar(jtext, var);
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

	public Root getRoot() {
		return helper.root;
	}

	/**
	 * Setzt den Sanduhr Mauscursor
	 */
	public void setWaitCursor() {
		helper.setWaitCursor();
	}

	/**
	 * Setzt wieder den "normalen" Mauscursor
	 */
	public void setNormalCursor() {
		helper.setNormalCursor();
	}

	public void adjustScrollingSpeed(JScrollPane scroll_panel) {
		helper.adjustScrollingSpeed(scroll_panel);
	}

	public void invokeDialogUnique(BaseDialogBase dialog) {
		helper.invokeDialogUnique(dialog);
	}

	public void registerOnCloseListener(Runnable runnable) {
		helper.registerOnCloseListener(runnable);
	}

	public boolean closeSubdialogsOnClose() {
		return true;
	}

	public Container getContainer() {
		return this;
	}

	public Collection<Pair> getBindVarPairs() {
		return helper.getBindVarPairs();
	}

	public void addBindVarPair(Pair pair) {
		helper.addBindVarPair(pair);
	}

	/**
	 * @return a Dialog identifier for saving some data, eg: width and height of
	 * the dialog. The default behavior is retuning the dialog title.
	 * This function should be overloaded if some instances of dialogs
	 * should all have the same e.g. size, but it's not possible, because
	 * each one has a different title.
	 */

	public String getUniqueDialogIdentifier() {
		return getTitle();
	}

	/**
	 * language the dialog is programmed in if not set, the settings from
	 * Root.getBaseLangague() are used
	 */
	public void setBaseLanguage(String language) {
		helper.setBaseLanguage(language);
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

}
