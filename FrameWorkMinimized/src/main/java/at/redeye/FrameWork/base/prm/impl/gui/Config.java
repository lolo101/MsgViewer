package at.redeye.FrameWork.base.prm.impl.gui;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Saveable;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.ConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.ConfigParamHook;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;
import at.redeye.FrameWork.base.prm.impl.PrmErrUtil;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;
import at.redeye.FrameWork.widgets.helpwindow.HelpWin;
import at.redeye.FrameWork.widgets.helpwindow.HelpWinHook;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Config extends BaseDialog implements Saveable, PrmListener {

    private static final long serialVersionUID = 1L;
    private final List<DBStrukt> values = new ArrayList<>();
    private final TableManipulator tm;

    /**
     * Creates new form Config
     */
    public Config(Root root) {
        super(root, "Einstellungen");
        setBaseLanguage("de");
        initComponents();

        DBConfig config = new DBConfig();

        tm = new TableManipulator(root.getSetup(), jTContent, config);

        tm.hide(config.hist.lo_user,
                config.hist.lo_zeit,
                config.hist.an_zeit,
                config.hist.an_user,
                config.hist.ae_zeit,
                config.hist.ae_user);

        tm.setEditable(config.value);
        tm.prepareTable();

        reloadConfig();

        tm.autoResize();

        // Register all local PRM
        ConfigDefinitions.entries.values().forEach(c -> c.addPrmListener(this));
    }

    private void reloadConfig() {

        values.clear();
        tm.clear();

        Collection<DBConfig> configs = root.loadConfig();

        tm.addAll(configs);
        values.addAll(configs);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLTitle = new javax.swing.JLabel();
        javax.swing.JButton jBHelp = new javax.swing.JButton();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTContent = new javax.swing.JTable();
        javax.swing.JButton jBSave = new javax.swing.JButton();
        javax.swing.JButton jBCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Einstellungen");

        jBHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/help.png"))); // NOI18N
        jBHelp.addActionListener(this::jBHelpActionPerformed);

        jTContent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTContent);

        jBSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/button_ok.gif"))); // NOI18N
        jBSave.setText("Speichern");
        jBSave.addActionListener(this::jBSaveActionPerformed);

        jBCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif"))); // NOI18N
        jBCancel.setText("Schließen");
        jBCancel.addActionListener(this::jBCancelActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addComponent(jBHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 395, Short.MAX_VALUE)
                        .addComponent(jBCancel)))
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
                    .addComponent(jBCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jBHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBHelpActionPerformed

    HelpWinHook hook = new ConfigParamHook(root, "LOCALSETTINGSHOOOK",
            ConfigDefinitions.help_search_path);

    invokeDialog(new HelpWin(root, "/at/redeye/FrameWork/base/resources/Help/", "LocalConfig", hook));

}//GEN-LAST:event_jBHelpActionPerformed

private void jBSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSaveActionPerformed

    saveData();
}//GEN-LAST:event_jBSaveActionPerformed

private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed

    if (canClose()) {
        Set<String> keys = ConfigDefinitions.entries.keySet();

        for (String key : keys) {
            ConfigDefinitions.get(key).removePrmListener(this);
        }
        close();
    }

}//GEN-LAST:event_jBCancelActionPerformed

    @Override
    public boolean canClose() {
        int ret = checkSave(tm);

        if (ret == 1) {
            ((Saveable) this).saveData();
            return true;
        }
        return ret != -1;
    }

    @Override
    public void saveData() {
        for (Integer i : tm.getEditedRows()) {
            DBConfig entry = (DBConfig) values.get(i);
            root.getSetup().setLocalConfig(entry.getConfigName(), entry.getConfigValue());
        }

        root.saveSetup();
        reloadConfig();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jTContent;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onChange(PrmDefaultChecksInterface checks, PrmActionEvent event) {
        if(!checks.doChecks(event)) {
            PrmErrUtil.displayPrmError(this, event.getParameterName());
            root.getSetup().setLocalConfig(event.getParameterName().toString(), event.getOldPrmValue().toString());

        }
    }

}

