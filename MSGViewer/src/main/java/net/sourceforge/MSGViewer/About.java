package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.widgets.helpwindow.HelpWin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class About extends BaseDialog {

    public About(Root root)
    {
        super(root,"About");
        initComponents();

        jLTitle.setText(MlM("About") + " " + MlM(root.getAppName()));
        String version = About.class.getPackage().getImplementationVersion();
        jLVersion.setText(MlM("Version") + " " + version);
    }

    private static ImageIcon getIcon(Module module, String name) throws IOException {
        InputStream resource = module.getResourceAsStream(name);
        Image image = ImageIO.read(resource);
        return new ImageIcon(image);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLTitle = new javax.swing.JLabel();
        javax.swing.JButton jBCancel = new javax.swing.JButton();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jLVersion = new javax.swing.JLabel();
        javax.swing.JButton jBLicence = new javax.swing.JButton();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("About Redeye Barcode Editor");

        jBCancel.setText("Close");
        jBCancel.addActionListener(this::jBCancelActionPerformed);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLVersion.setFont(new java.awt.Font("Dialog", Font.BOLD, 14)); // NOI18N
        jLVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLVersion.setText("Version");

        jBLicence.setText("Licence");
        jBLicence.addActionListener(this::jBLicenceActionPerformed);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Licence: GPL Version 3");

        ModuleLayer.boot().findModule("FrameWorkMinimized").ifPresentOrElse(
                module -> {
                    try {
                        jBCancel.setIcon(getIcon(module, "/at/redeye/FrameWork/base/resources/icons/fileclose.gif"));
                        jLabel1.setIcon(getIcon(module, "/at/redeye/FrameWork/base/resources/pictures/redeye.png"));
                        jBLicence.setIcon(getIcon(module, "/at/redeye/FrameWork/base/resources/icons/history.png"));
                    } catch (IOException ignore) {
                    }
                },
                () -> {
                    jBCancel.setIcon(new ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif")));
                    jLabel1.setIcon(new ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/pictures/redeye.png")));
                    jBLicence.setIcon(new ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/history.png")));
                }
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jBLicence)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 305, Short.MAX_VALUE)
                        .addComponent(jBCancel)
                        .addContainerGap())
                    .addComponent(jLVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitle)
                .addGap(4, 4, 4)
                .addComponent(jLVersion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancel)
                    .addComponent(jBLicence))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed
    if( canClose() )
        close();
}//GEN-LAST:event_jBCancelActionPerformed

private void jBLicenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLicenceActionPerformed

    invokeDialogUnique(new HelpWin(root, "Licence"));

}//GEN-LAST:event_jBLicenceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLTitle;
    private javax.swing.JLabel jLVersion;
    // End of variables declaration//GEN-END:variables

}
