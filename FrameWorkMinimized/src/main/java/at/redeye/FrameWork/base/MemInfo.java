package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.FreeMemory;
import at.redeye.FrameWork.utilities.MemoryInformation;

import java.awt.*;


public class MemInfo extends BaseDialog {

    public MemInfo(Root root) {
        super(root, "Speicherinformationen");
        setBaseLanguage("de");
        initComponents();

        reload();
    }

    private void reload()
    {

        String info = FreeMemory.getMeminfo() +
                "\n------------------------\n" +
                MemoryInformation.createMemoryInfo();
        jTextMeminfo.setText(info);
        jTextMeminfo.setCaretPosition(0);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLTitle = new javax.swing.JLabel();
        javax.swing.JButton jBCancel = new javax.swing.JButton();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTextMeminfo = new javax.swing.JTextArea();
        javax.swing.JButton jButtonReaload = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Speicherinformationen");

        jBCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif"))); // NOI18N
        jBCancel.setText("Schließen");
        jBCancel.addActionListener(this::jBCancelActionPerformed);

        jTextMeminfo.setColumns(20);
        jTextMeminfo.setRows(5);
        jScrollPane1.setViewportView(jTextMeminfo);

        jButtonReaload.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12)); // NOI18N
        jButtonReaload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/reload.png"))); // NOI18N
        jButtonReaload.setText("Aktualisieren");
        jButtonReaload.addActionListener(this::jButtonRealoadActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonReaload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(jBCancel))
                    .addComponent(jLTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancel)
                    .addComponent(jButtonReaload))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed
    if( canClose() )
        close();
}//GEN-LAST:event_jBCancelActionPerformed

private void jButtonRealoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRealoadActionPerformed
// TODO add your handling code here:
    reload();
}//GEN-LAST:event_jButtonRealoadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea jTextMeminfo;
    // End of variables declaration//GEN-END:variables

}
