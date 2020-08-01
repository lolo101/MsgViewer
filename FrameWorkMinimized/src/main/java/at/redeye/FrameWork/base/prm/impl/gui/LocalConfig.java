/*
 * Config.java
 *
 * Created on 13. März 2009, 09:04
 */
package at.redeye.FrameWork.base.prm.impl.gui;

import at.redeye.FrameWork.base.prm.impl.*;
import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.prm.PrmCustomChecksInterface;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;
import at.redeye.FrameWork.widgets.helpwindow.HelpWin;
import at.redeye.FrameWork.widgets.helpwindow.HelpWinHook;

import java.awt.*;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author  martin
 */
public class LocalConfig extends BaseDialog implements CanCloseInterface, PrmListener {

    private static final long serialVersionUID = 1L;
    Vector<DBStrukt> values = new Vector<>();
    TableManipulator tm;

    /** Creates new form Config */
    public LocalConfig(Root root) {
        super(root, "Lokale Einstellungen");
        setBaseLanguage("de");
        initComponents();

        DBConfig config = new DBConfig();

        tm = new TableManipulator(root, jTContent, config);

        tm.hide(config.hist.lo_user);
        tm.hide(config.hist.lo_zeit);
        tm.hide(config.hist.an_zeit);
        tm.hide(config.hist.an_user);
        tm.hide(config.hist.ae_zeit);
        tm.hide(config.hist.ae_user);

        tm.setEditable(config.value);
        tm.setAutoCompleteForAllOfThisColl(config.value, false);

        tm.prepareTable();

        feed_table();

        tm.autoResize();

        // Register all local PRM
        Set<String> keys = LocalConfigDefinitions.entries.keySet();
        for (String key : keys) {
            LocalConfigDefinitions.get(key).addPrmListener(this);
        }
    }

    private void feed_table() {

        values.clear();
        tm.clear();

        TreeMap<String, DBConfig> vals = new TreeMap<>();

        Set<String> keys = LocalConfigDefinitions.entries.keySet();

        for (String key : keys) {
            vals.put(key, LocalConfigDefinitions.get(key));
        }

        for (String key : keys) {

            DBConfig c = (DBConfig) vals.get(key).getCopy();

            String val = root.getSetup().getLocalConfig(c.getConfigName(), c.getConfigValue());

            c.descr.loadFromCopy(MlM(c.descr.getValue()));

            c.setConfigValue(val);
            tm.add(c);
            values.add(c);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLTitle = new javax.swing.JLabel();
        jBHelp = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTContent = new javax.swing.JTable();
        jBSave = new javax.swing.JButton();
        jBCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18));
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Lokale Einstellungen");

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addComponent(jBHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 391, Short.MAX_VALUE)
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

    HelpWinHook hook = new ConfigParamHook(root, "LOCALSETTINGSHOOOK", false,
            LocalConfigDefinitions.help_search_path);

    invokeDialog(new HelpWin(root, "/at/redeye/FrameWork/base/resources/Help/", "LocalConfig", hook));

}//GEN-LAST:event_jBHelpActionPerformed

private void jBSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSaveActionPerformed

    saveData();
}//GEN-LAST:event_jBSaveActionPerformed

private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed

    if (canClose()) {
        Set<String> keys = LocalConfigDefinitions.entries.keySet();

        for (String key : keys) {
            LocalConfigDefinitions.get(key).removePrmListener(this);
        }
        close();
    }

}//GEN-LAST:event_jBCancelActionPerformed

    @Override
    public boolean canClose() {
        return DefaultCanClose.DefaultCanCloseWithTable(this, tm);
    }

    public void saveData() {
        for (Integer i : tm.getEditedRows()) {
            DBConfig entry = (DBConfig) values.get(i);
            root.getSetup().setLocalConfig(entry.getConfigName(), entry.getConfigValue());
        }

        root.saveSetup();
        feed_table();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancel;
    private javax.swing.JButton jBHelp;
    private javax.swing.JButton jBSave;
    private javax.swing.JLabel jLTitle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTContent;
    // End of variables declaration//GEN-END:variables

    public void onChange(PrmDefaultChecksInterface checks, PrmActionEvent event) {
        if(checks.doChecks(event) == false) {
          PrmErrUtil.displayPrmError(this, event.getParameterName().toString());
          root.getSetup().setLocalConfig(event.getParameterName().toString(), event.getOldPrmValue().toString());

      }
    }

    public void onChange(PrmCustomChecksInterface customChecks, PrmActionEvent event) {
        if(customChecks.doCustomChecks(event) == false) {
          PrmErrUtil.displayPrmError(this, event.getParameterName().toString());
          root.getSetup().setLocalConfig(event.getParameterName().toString(), event.getOldPrmValue().toString());

      }
    }
}

