package at.redeye.FrameWork.base.prm.impl.gui;

import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.prm.PrmCustomChecksInterface;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.ConfigParamHook;
import at.redeye.FrameWork.base.prm.impl.GlobalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;
import at.redeye.FrameWork.base.prm.impl.PrmErrUtil;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;
import at.redeye.FrameWork.widgets.helpwindow.HelpWin;
import at.redeye.FrameWork.widgets.helpwindow.HelpWinHook;

import java.awt.*;
import java.util.List;
import java.util.*;

public class GlobalConfig extends BaseDialog implements Saveable,
        PrmListener {

    private static final long serialVersionUID = 1L;
    final Vector<DBStrukt> values = new Vector<>();
    final TableManipulator tm;
    final GlobalConfig myself;

    /**
     * Creates new form Config
     */
    public GlobalConfig(Root root) {
        super(root, "Globale Einstellungen");
        setBaseLanguage("de");
        initComponents();

        DBConfig config = new DBConfig();

        tm = new TableManipulator(root, jTContent, config);

        tm.hide(config.hist.lo_user);
        tm.hide(config.hist.lo_zeit);

        tm.setEditable(config.value);
        tm.setAutoCompleteForAllOfThisColl(config.value, false);

        tm.prepareTable();

        feed_table(false);

        tm.autoResize();

        Set<String> keys = GlobalConfigDefinitions.entries.keySet();
        for (String key : keys) {
            DBConfig c = root.getSetup().getConfig(key);
            if (c != null) {
                c.addPrmListener(this);
                // Set PRM properties : -> Ugly programm in LocalSetup
                c.setCustomChecks(GlobalConfigDefinitions.get(key)
                        .getCustomChecks());
                c.setDefaultChecks(GlobalConfigDefinitions.get(key)
                        .getDefaultChecks());
                c.setPossibleValues(GlobalConfigDefinitions.get(key)
                        .getPossibleValues());
                c.addAllPrmListeners(GlobalConfigDefinitions.get(key));
            } else {
                logger.warn("PRM " + key + " not found in LocalSetup!");
            }
        }
        myself = this;

    }

    public final void feed_table(boolean autombox) {
        if (autombox) {
            new AutoMBox<>("GlobalConfig", () -> feed_table()).run();
        } else {
            new AutoLogger<>("GlobalConfig", () -> feed_table()).run();
        }
    }

    private void feed_table() {
        values.clear();
        tm.clear();

        Map<String, DBConfig> vals = new TreeMap<>(GlobalConfigDefinitions.entries);

        // nun alle Einträge aus der DB dazumergen

        List<DBStrukt> res = getTransaction()
                .fetchTable(new DBConfig());

        for (DBStrukt s : res) {
            DBConfig c = (DBConfig) s;
            vals.put(c.getConfigName(), c);
        }

        Collection<DBConfig> configs = vals.values();

        configs.stream()
                .map(c -> c.descr)
                .forEach(descr -> descr.loadFromCopy(MlM(descr.getValue())));

        values.addAll(configs);
        tm.addAll(configs);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLTitle = new javax.swing.JLabel();
        javax.swing.JButton jBHelp = new javax.swing.JButton();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTContent = new javax.swing.JTable();
        javax.swing.JButton jBSave = new javax.swing.JButton();
        javax.swing.JButton jBClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Globale Einstellungen");

        jBHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/help.png"))); // NOI18N
        jBHelp.addActionListener(this::jBHelpActionPerformed);

        jTContent.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(jTContent);

        jBSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/button_ok.gif"))); // NOI18N
        jBSave.setText("Speichern");
        jBSave.addActionListener(this::jBSaveActionPerformed);

        jBClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif"))); // NOI18N
        jBClose.setText("Schließen");
        jBClose.addActionListener(this::jBCloseActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                                                .addGap(4, 4, 4)
                                                .addComponent(jBHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jBSave)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 391, Short.MAX_VALUE)
                                                .addComponent(jBClose)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jBHelp)
                                        .addComponent(jLTitle))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jBSave)
                                        .addComponent(jBClose))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBHelpActionPerformed

        java.awt.EventQueue.invokeLater(new Runnable() {

            final HelpWinHook hook = new ConfigParamHook(root, "GLOBALSETTINGSHOOOK",
                    true, GlobalConfigDefinitions.help_search_path);

            public void run() {
                new HelpWin(root, "/at/redeye/FrameWork/base/resources/Help/",
                        "GlobalConfig", hook).setVisible(true);
            }
        });
    }//GEN-LAST:event_jBHelpActionPerformed

    private void jBSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSaveActionPerformed

        saveData();

    }//GEN-LAST:event_jBSaveActionPerformed

    @Override
    public boolean canClose() {
        return DefaultCanClose.DefaultCanCloseWithTable(this, tm);
    }

    public void saveData() {
        for (Integer editedRow : tm.getEditedRows()) {
            DBConfig entry = (DBConfig) values.get(editedRow);
            root.getSetup().setConfig(entry.getConfigName(),
                    entry.getConfigValue());
        }

        root.saveSetup();
        feed_table(true);
    }

    private void jBCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCloseActionPerformed
        new AutoMBox<>(getTitle(), () -> {
            if (canClose()) {
                getTransaction().rollback();
                Set<String> keys = GlobalConfigDefinitions.entries.keySet();
                for (String key : keys) {
                    DBConfig c = root.getSetup().getConfig(key);
                    if (c != null) {
                        c.removePrmListener(myself);
                    }
                }
                close();
            }
        }).run();

    }//GEN-LAST:event_jBCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jTContent;
    // End of variables declaration//GEN-END:variables

    public void onChange(PrmDefaultChecksInterface checks, PrmActionEvent event) {
        if (!checks.doChecks(event)) {
            PrmErrUtil.displayPrmError(this, event.getParameterName()
                    .toString());
            PrmErrUtil.restoreGlobalPrm(this, event.getParameterName()
                    .toString(), event.getOldPrmValue().toString());

        }

    }

    public void onChange(PrmCustomChecksInterface customChecks,
                         PrmActionEvent event) {

        if (!customChecks.doCustomChecks(event)) {
            PrmErrUtil.displayPrmError(this, event.getParameterName()
                    .toString());
            PrmErrUtil.restoreGlobalPrm(this, event.getParameterName()
                    .toString(), event.getOldPrmValue().toString());
        }
    }
}
