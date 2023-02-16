package at.redeye.FrameWork.Plugin;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.widgets.helpwindow.HyperlinkExecuter;

import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.util.List;

public class AboutPlugins extends BaseDialog {

    public AboutPlugins( Root root ) {
        super(root, "Plugins" );
        setBaseLanguage("de");

        initComponents();

        List<Plugin> plugins = root.getPlugins().registered();

        jLPlugins.setListData(plugins.toArray(Plugin[]::new));

        if( !plugins.isEmpty() )
        {
            jLPlugins.setSelectedIndex(0);
            jLPluginsMouseClicked(null);
        }

        HyperlinkListener executer = new HyperlinkExecuter();

        jTChangeLog.addHyperlinkListener(executer);
        jTLicenceText.addHyperlinkListener(executer);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLTitle = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jLPlugins = new javax.swing.JList<>();
        javax.swing.JTabbedPane jTPlugin = new javax.swing.JTabbedPane();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        jTLicenceText = new javax.swing.JEditorPane();
        javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
        jTChangeLog = new javax.swing.JEditorPane();
        javax.swing.JButton jBCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Installierte Erweiterungen");

        jLPlugins.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jLPlugins.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLPluginsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jLPlugins);

        jTLicenceText.setContentType("text/html"); // NOI18N
        jTLicenceText.setEditable(false);
        jScrollPane2.setViewportView(jTLicenceText);

        jTPlugin.addTab("Lizenz", jScrollPane2);

        jTChangeLog.setContentType("text/html"); // NOI18N
        jTChangeLog.setEditable(false);
        jScrollPane3.setViewportView(jTChangeLog);

        jTPlugin.addTab("Änderungsprotokoll", jScrollPane3);

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
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jBCancel)
                                                        .addComponent(jTPlugin, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)))
                                        .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                    .addComponent(jTPlugin, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLPluginsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLPluginsMouseClicked

        Plugin plugin = jLPlugins.getSelectedValue();

        if(plugin == null)
            return;

        jTLicenceText.setText(plugin.getLicenceText());
        jTLicenceText.setCaretPosition(0);

        jTChangeLog.setText(plugin.getChangeLog());
        jTChangeLog.setCaretPosition(0);

    }//GEN-LAST:event_jLPluginsMouseClicked

    private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed
        if( canClose() )
            close();
}//GEN-LAST:event_jBCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<Plugin> jLPlugins;
    private javax.swing.JEditorPane jTChangeLog;
    private javax.swing.JEditorPane jTLicenceText;
    // End of variables declaration//GEN-END:variables

}
