package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBFlagInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;
import at.redeye.FrameWork.base.translation.TranslationHelper;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.FrameWork.widgets.NoticeIfChangedTextField;
import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class BaseDialogBaseHelper implements BindVarInterface {
    protected final Root root;

    protected final String title;

    protected static Logger logger = LogManager.getLogger(BaseDialogBaseHelper.class);
    private Timer autoRefreshTimer;
    private TimerTask autoRefreshTask;

    private boolean edited;
    private BindVarInterface bind_vars;
    private List<Runnable> onCloseListeners;
    private final CloseSubDialogHelper close_subdialog_helper;

    /**
     * All keys ESC, or F1, F2 listeners are registered in this container
     */
    private final Map<KeyStroke, Vector<Runnable>> listen_key_events = new HashMap<>();
    private final JRootPane myrootPane;
    private Runnable HelpWinRunnable;
    private final UniqueDialogHelper unique_dialog_helper = new UniqueDialogHelper();
    private TranslationHelper translation_helper;
    private boolean autoswitch_trans_first_run = true;

    private static int default_pos_x = 300;
    private static int default_pos_y = 300;
    /**
     * language the dialog is programmed in if not set, the settings from
     * Root.getBaseLangague() are used
     */
    private String base_language;

    private class ActionKeyListener implements ActionListener {
        private final KeyStroke key;

        private ActionKeyListener(KeyStroke key) {
            this.key = key;
        }

        public void actionPerformed(ActionEvent e) {
            listen_key_events.get(key).forEach(Runnable::run);
        }
    }

    private final BaseDialogBase parent;

    public BaseDialogBaseHelper(final BaseDialogBase parent, Root root,
                                String title, JRootPane myrootPane, boolean do_not_inform_root) {
        this.parent = parent;
        this.root = root;
        this.title = title;
        this.myrootPane = myrootPane;
        this.close_subdialog_helper = new CloseSubDialogHelper(parent);

        initCommon(do_not_inform_root);
    }

    private void initCommon(boolean do_not_inform_root) {
        translation_helper = new TranslationHelper(root, parent, this);
        parent.setTitle(MlM(title));

        root.loadMlM4Class(this, "de");

        if (!do_not_inform_root)
            root.getDialogs().informWindowOpened(parent);

        if (logger.isDebugEnabled()) {
            logger.debug(title);
        }

        parent.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parent.canClose()) {
                    parent.close();
                }
            }
        });

        String id = parent.getUniqueDialogIdentifier();

        int x = Integer.parseInt(root.getSetup().getConfig(
                id.concat(Setup.WindowX), String.valueOf(default_pos_x += 30)));
        int y = Integer.parseInt(root.getSetup().getConfig(
                id.concat(Setup.WindowY), String.valueOf(default_pos_y += 30)));
        int w = Integer.parseInt(root.getSetup().getConfig(
                id.concat(Setup.WindowWidth), "0"));
        int h = Integer.parseInt(root.getSetup().getConfig(
                id.concat(Setup.WindowHeight), "0"));

        Dimension dim = getVirtualScreenSize();

        Point mouse_point = MouseInfo.getPointerInfo().getLocation();

        if ((mouse_point.x <= x || x + w <= mouse_point.x)
                && Math.abs(x + w - mouse_point.x) >= w) {
            x = mouse_point.x - 100;
        }

        if ((mouse_point.y <= y || y + h <= mouse_point.y)
                && Math.abs(y + h - mouse_point.y) >= h) {
            y = mouse_point.y - 100;
        }

        if (dim.getWidth() < x + parent.getWidth())
            x = 100;

        if (dim.getHeight() < y + parent.getHeight())
            y = 100;

        if (x < 0)
            x = 100;

        if (y < 0)
            y = 100;

        logger.info("setting bounds to: " + x + "x" + y);
        parent.setBounds(x, y, 0, 0);
        logger.info("position now: " + parent.getX() + "x" + parent.getY());

        if (w > 0 && h > 0 && parent.openWithLastWidthAndHeight()) {
            logger.info(String.format("x (%d) + w (%d) = %d dim.Width: %d", x,
                    w, x + w, (int) dim.getWidth()));

            if (x + w > dim.getWidth()) {
                logger.info("reducing with");
                w = (int) dim.getWidth() - x;
            }

            logger.info(String.format("y (%d) + h (%d) = %d dim.Height: %d", y,
                    h, y + h, (int) dim.getHeight()));

            if (y + h > dim.getHeight()) {
                logger.info("reducing height");
                h = (int) dim.getHeight() - y;
            }

            logger.info("set size to: " + w + "x" + h);
            parent.setPreferredSize(new Dimension(w, h));
        }

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                () -> {
                    if (parent.canClose()) {
                        parent.close();
                    }
                });
        loadStuff();
    }

    /* should be removed later */
    private void loadStuff() {
        StringUtils.set_defaultAutoLineLenght(Integer.parseInt(root.getSetup()
                .getConfig(
                        FrameWorkConfigDefinitions.DefaultAutoLineBreakWidth)));
    }

    /**
     * automatically opens the Help Windows, when F1 is pressed
     *
     * @param runnable This runnable should open the Help Window
     */
    public void registerHelpWin(Runnable runnable) {
        HelpWinRunnable = runnable;

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
                runnable);
    }

    /**
     * returns the virtual screensize in a multimonitor system
     */
    private static Dimension getVirtualScreenSize() {
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for (GraphicsDevice gd : gs) {
            GraphicsConfiguration[] gc = gd.getConfigurations();
            for (GraphicsConfiguration graphicsConfiguration : gc) {
                virtualBounds = virtualBounds.union(graphicsConfiguration.getBounds());
            }
        }
        return virtualBounds.getSize();
    }

    /**
     * opens the registerd Help win by Hand
     */
    public void callHelpWin() {
        if (HelpWinRunnable != null) {
            setWaitCursor();
            HelpWinRunnable.run();
            setNormalCursor();
        }
    }

    /**
     * Setzt den Sanduhr Mauscursor
     */
    public void setWaitCursor() {
        setWaitCursor(true);
    }

    /**
     * Setzt den Sanduhr, oder "normale" Mauscursor
     *
     * @param state <b>true</b> für die Sanduhr und <b>false</b> für den nurmalen
     *              Cursor
     */
    public void setWaitCursor(boolean state) {
        if (state)
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else
            parent.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Setzt wieder den "normalen" Mauscursor
     */
    public void setNormalCursor() {
        setWaitCursor(false);
    }

    /**
     * Konfiguriert das jScrollpanel entsprechen dem im Setup hinterlegten
     * Geschwindigkeit. Vom User über den Parameter VerticalScrollingSpeed
     * einstellbar.
     */
    public void adjustScrollingSpeed(JScrollPane scroll_panel) {
        try {
            adjustScrollingSpeed(scroll_panel.getVerticalScrollBar(),
                    BaseAppConfigDefinitions.VerticalScrollingSpeed);
            adjustScrollingSpeed(scroll_panel.getVerticalScrollBar(),
                    BaseAppConfigDefinitions.HorizontalScrollingSpeed);
        } catch (NumberFormatException ex) {
            logger.error(ex);
        }
    }

    private void adjustScrollingSpeed(Adjustable scrollBar, DBConfig config) {
        String value = root.getSetup().getConfig(config);

        int i = Integer.parseInt(value);

        if (i <= 0) {
            logger.error("invalid scrolling interval: " + i
                    + " using default value: " + config.getConfigValue());
            i = Integer.parseInt(config.getConfigValue());
        }

        scrollBar.setUnitIncrement(i);
    }

    /**
     * Little helper function that sets the frame visible and push it to front,
     * by useing the wait cursor.
     */
    public void invokeDialog(BaseDialogBase dlg) {
        setWaitCursor();
        dlg.setVisible(true);
        dlg.toFront();

        if (parent.closeSubdialogsOnClose())
            close_subdialog_helper.closeSubDialog(dlg);

        setNormalCursor();
    }

    public void invokeDialogUnique(BaseDialogBase dialog) {
        setWaitCursor();

        BaseDialogBase d_unique = unique_dialog_helper
                .invokeUniqueDialog(dialog);
        d_unique.setVisible(true);
        d_unique.toFront();

        if (parent.closeSubdialogsOnClose())
            close_subdialog_helper.closeSubDialog(dialog);

        setNormalCursor();
    }

    public void registerOnCloseListener(Runnable runnable) {
        if (runnable == null)
            return;

        if (onCloseListeners == null)
            onCloseListeners = new LinkedList<>();

        onCloseListeners.add(runnable);
    }

    public void setEdited() {
        setEdited(true);
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean val) {
        edited = val;

        if (!edited) {
            setBindVarsChanged();
        }
    }

    /**
     * Checks, if data within the table have been change, asks the user what
     * sould be done (save it, don't save it, or cancel current operation
     *
     * @param tm TableManipulator object
     * @return 1 when the data should be saved <br/>
     * 0 on saving should be done <br/>
     * -1 cancel current operation <br/>
     */
    public int checkSave(TableManipulator tm) {
        tm.stopEditing();

        if (tm.getEditedRows().isEmpty() && !edited) {
            return 0;
        }
        return checkSave();
    }

    /**
     * @return 1 on Save Data <br/>
     * 0 on Don't Save <br/>
     * -1 on Cancel <br/>
     */
    public int checkSave() {
        Object[] options = {MlM("Daten Speichern"),
                MlM("Änderungen verwerfen"), MlM("Abbrechen")};

        int n = JOptionPane
                .showOptionDialog(
                        null,
                        StringUtils
                                .autoLineBreak(MlM("Sie haben Daten verändert. "
                                        + "Möchten Sie die Daten vor dem Verlassen des Dialoges speichern?")),
                        parent.getTitle(), JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        switch (n) {
            case JOptionPane.YES_OPTION:
                return 1;
            case JOptionPane.NO_OPTION:
                return 0;
            default:
                return -1;
        }
    }

    /**
     * closes the current dialog.
     */
    public void close() {

        cancelAutoRefreshTimer();

        String id_xy = parent.getUniqueDialogIdentifier();
        String id_wh = parent.getUniqueDialogIdentifier();

        logger.info("store size to: " + parent.getWidth() + "x"
                + parent.getHeight());

        root.getSetup().setLocalConfig(id_xy.concat(Setup.WindowX),
                Integer.toString(parent.getX()));
        root.getSetup().setLocalConfig(id_xy.concat(Setup.WindowY),
                Integer.toString(parent.getY()));
        root.getSetup().setLocalConfig(id_wh.concat(Setup.WindowWidth),
                Integer.toString(parent.getWidth()));
        root.getSetup().setLocalConfig(id_wh.concat(Setup.WindowHeight),
                Integer.toString(parent.getHeight()));

        if (onCloseListeners != null) {
            for (Runnable run : onCloseListeners)
                run.run();

            onCloseListeners.clear();
        }

        root.getDialogs().informWindowClosed(parent);

        parent.dispose();
    }

    private void registerActionKeyListenerOnRootPane(KeyStroke key) {
        if (myrootPane == null)
            return;

        myrootPane.registerKeyboardAction(new ActionKeyListener(key), key,
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Registers a listener for a F1, ESC, or something global keypressed Event
     *
     * @param to_listen_Key Keyboard Key
     * @param runnable      Method to call
     */
    public void registerActionKeyListener(KeyStroke to_listen_Key,
                                          Runnable runnable) {
        Vector<Runnable> listeners = listen_key_events.get(to_listen_Key);

        if (listeners == null) {
            listeners = new Vector<>();
            listen_key_events.put(to_listen_Key, listeners);

            registerActionKeyListenerOnRootPane(to_listen_Key);
        }

        listeners.add(runnable);
    }

    /**
     * in jTextField an einen StringBuffer anbinden
     *
     * @param jtext das Textfeld
     * @param var   der StringBuffer
     *              <p>
     *              Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
     *              dann der demenstprechende Inhalt entweder vom GUI zu
     *              Variablen, oder umgekehrt übertragen.
     */
    @Override
    public void bindVar(JTextField jtext, StringBuffer var) {

        checkBindVars();

        bind_vars.bindVar(jtext, var);
    }

    /**
     * in jTextArea an eine StringBuffer anbinden
     *
     * @param jtext das Textfeld
     * @param var   der StringBuffer
     *              <p>
     *              Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird dann der
     *              demenstprechende Inhalt entweder vom GUI zu Variablen, oder umgekehrt
     *              übertragen.
     */
    @Override
    public void bindVar(JTextArea jtext, StringBuffer var) {
        checkBindVars();
        bind_vars.bindVar(jtext, var);
    }

    /**
     * in jTextArea an eine DBValue anbinden
     *
     * @param jtext das Textfeld
     * @param var   der DBValue
     *              <p>
     *              Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird dann der
     *              demenstprechende Inhalt entweder vom GUI zu Variablen, oder umgekehrt
     *              übertragen.
     */
    @Override
    public void bindVar(JTextArea jtext, DBValue var) {
        checkBindVars();
        bind_vars.bindVar(jtext, var);
    }

    /**
     * Ein jTextField an eine DBValue anbinden
     *
     * @param jtext das Textfeld
     * @param var   die Datenbankvariable
     *              <p>
     *              Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
     *              dann der demenstprechende Inhalt entweder vom GUI zu
     *              Variablen, oder umgekehrt übertragen.
     */
    @Override
    public void bindVar(JTextField jtext, DBValue var) {

        checkBindVars();

        bind_vars.bindVar(jtext, var);
    }

    /**
     * Eine {@link JComboBox} an eine {@link DBValue} anbinden
     *
     * @param jcombo das Textfeld
     * @param var    die Datenbankvariable
     *               <p>
     *               Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
     *               dann der demenstprechende Inhalt entweder vom GUI zu
     *               Variablen, oder umgekehrt übertragen.
     */
    @Override
    public void bindVar(JComboBox<?> jcombo, DBValue var) {

        checkBindVars();

        bind_vars.bindVar(jcombo, var);
    }

    /**
     * Eine {@link IDateTimeComponent} an eine {@link DBDateTime} Variable
     * anbinden
     *
     * @param comp     die DateTime Komponente
     * @param dateTime die Datenbankvariable
     *                 <p>
     *                 Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
     *                 dann der demenstprechende Inhalt entweder vom GUI zu
     *                 Variablen, oder umgekehrt übertragen.
     */
    public void bindVar(IDateTimeComponent comp, DBDateTime dateTime) {

        checkBindVars();

        bind_vars.bindVar(comp, dateTime);
    }

    /**
     * Eine JCheckBox an eine DBFlagInteger Variable anbinden
     *
     * @param jtext die Textbox
     * @param var   die Datebanvariable
     *              <p>
     *              Bei einem Aufruf von var_to_gui(), oder gui_to_var(), wird
     *              dann der demenstprechende Inhalt entweder vom GUI zu
     *              Variablen, oder umgekehrt übertragen.
     */
    @Override
    public void bindVar(JCheckBox jtext, DBFlagInteger var) {

        checkBindVars();

        bind_vars.bindVar(jtext, var);
    }

    /**
     * Alle Werte der angebunden Variablen in die entsprechenden GUI Komponenten
     * übertragen
     */
    @Override
    public void var_to_gui() {

        if (bind_vars == null)
            return;

        bind_vars.var_to_gui();
    }

    /**
     * Alle Elemnte des GUIs in die angebundenen Datenbankfelder kopieren
     */
    @Override
    public void gui_to_var() {

        if (bind_vars == null)
            return;

        bind_vars.gui_to_var();
    }

    private void setBindVarsChanged() {

        if (bind_vars == null)
            return;

        for (Pair pair : bind_vars.getBindVarPairs()) {
            if (pair.get_first() instanceof NoticeIfChangedTextField) {
                NoticeIfChangedTextField text_field = (NoticeIfChangedTextField) pair
                        .get_first();

                text_field.setChanged(false);
            }
        }
    }

    @Override
    public Collection<Pair> getBindVarPairs() {

        checkBindVars();

        return bind_vars.getBindVarPairs();
    }

    @Override
    public void addBindVarPair(Pair pair) {
        checkBindVars();

        bind_vars.addBindVarPair(pair);
    }

    private void checkBindVars() {
        if (bind_vars == null)
            bind_vars = new BindVarBase();
    }

    /**
     * language the dialog is programmed in if not set, the settings from
     * Root.getBaseLangague() are used
     */
    public void setBaseLanguage(String language) {
        base_language = language;
    }

    /**
     * @return language the dialog is programmed in if not set, the settings
     * from Root.getBaseLangague() are used
     */
    public String getBaseLanguage() {
        if (base_language == null)
            return root.getBaseLanguage();

        return base_language;
    }

    private void autoSwitchToCurrentLocale() {
        translation_helper.autoSwitchToCurrentLocale();
    }

    public void doLayout() {
        if (autoswitch_trans_first_run) {
            autoSwitchToCurrentLocale();
            autoswitch_trans_first_run = false;
        }
    }

    /**
     * @param message native langauge message
     * @return translated message, if available
     */
    public String MlM(String message) {
        return translation_helper.MlM(message);
    }

    private void cancelAutoRefreshTimer() {

        if (autoRefreshTask != null) {
            autoRefreshTask.cancel();
            autoRefreshTask = null;
        }

        if (autoRefreshTimer != null) {
            autoRefreshTimer.cancel();
            autoRefreshTimer = null;
        }
    }
}
