package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.Plugin.AboutPlugins;
import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterJob;
import java.io.File;

public class MainWin extends BaseWin {
    private final PrinterJob printerJob = PrinterJob.getPrinterJob();

    public MainWin(Root root) {
        super(root);
        initComponents();

        viewerPanel.setRoot(root, this);
        viewerPanel.setOpenNewMailInterface(this::openMail);

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), () -> {
            if (jMDetail.isEnabled()) jMDetailActionPerformed(null);
        });

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), () -> {
            if (jMNav.isEnabled()) jMNavActionPerformed(null);
        });
    }

    @Override
    public void openFile(File file) {
        viewerPanel.view(file.getPath());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewerPanel = new net.sourceforge.MSGViewer.ViewerPanel();
        printerJob.setPrintable(viewerPanel);
        menubar = new javax.swing.JMenuBar();
        javax.swing.JMenu jMenuProgram = new javax.swing.JMenu();
        javax.swing.JMenuItem jMOpenFile = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMSaveAs = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMPrint = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMSettings = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMQuit = new javax.swing.JMenuItem();
        javax.swing.JMenu jMenuInfo = new javax.swing.JMenu();
        jMDetail = new javax.swing.JMenuItem();
        jMNav = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem jMAbout = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMChangeLog = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMPlugin = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 400));

        viewerPanel.addPropertyChangeListener(this::viewerPanelPropertyChange);

        jMenuProgram.setText("Program");

        jMOpenFile.setText("File Open ...");
        jMOpenFile.addActionListener(this::jMOpenFileActionPerformed);
        jMenuProgram.add(jMOpenFile);

        jMSaveAs.setText("Save File as ...");
        jMSaveAs.addActionListener(this::jMSaveAsActionPerformed);
        jMenuProgram.add(jMSaveAs);

        jMPrint.setText("Print...");
        jMPrint.addActionListener(this::jMPrintActionPerformed);
        jMenuProgram.add(jMPrint);

        jMSettings.setText("Options");
        jMSettings.addActionListener(this::jMSettingsActionPerformed);
        jMenuProgram.add(jMSettings);

        jMQuit.setText("Quit");
        jMQuit.addActionListener(this::jMQuitActionPerformed);
        jMenuProgram.add(jMQuit);

        menubar.add(jMenuProgram);

        jMenuInfo.setText("Info");

        jMDetail.setText("Details");
        jMDetail.setEnabled(false);
        jMDetail.addActionListener(this::jMDetailActionPerformed);
        jMenuInfo.add(jMDetail);

        jMNav.setText("MSG Navigator");
        jMNav.setEnabled(false);
        jMNav.addActionListener(this::jMNavActionPerformed);
        jMenuInfo.add(jMNav);
        jMenuInfo.add(jSeparator1);

        jMAbout.setText("About");
        jMAbout.addActionListener(this::jMAboutActionPerformed);
        jMenuInfo.add(jMAbout);

        jMChangeLog.setText("Changelog");
        jMChangeLog.addActionListener(this::jMChangeLogActionPerformed);
        jMenuInfo.add(jMChangeLog);

        jMPlugin.setText("Plugins");
        jMPlugin.addActionListener(this::jMPluginActionPerformed);
        jMenuInfo.add(jMPlugin);

        menubar.add(jMenuInfo);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(viewerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(viewerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMAboutActionPerformed
        invokeDialogUnique(new About(root));
    }//GEN-LAST:event_jMAboutActionPerformed

    private void jMChangeLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMChangeLogActionPerformed

        invokeDialogUnique(new LocalHelpWin(root, "ChangeLog"));
    }//GEN-LAST:event_jMChangeLogActionPerformed

    private void jMPluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMPluginActionPerformed

        invokeDialogUnique(new AboutPlugins(root));
    }//GEN-LAST:event_jMPluginActionPerformed

    private void jMDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMDetailActionPerformed

        invokeDialogUnique(new Internals(root, viewerPanel.getMessage()));

    }//GEN-LAST:event_jMDetailActionPerformed

    private void jMNavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMNavActionPerformed

        invokeDialogUnique(new MSGNavigator(root, new File(viewerPanel.getFileName())));

    }//GEN-LAST:event_jMNavActionPerformed

    private void jMQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMQuitActionPerformed

        close();

    }//GEN-LAST:event_jMQuitActionPerformed

    private void jMSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMSettingsActionPerformed

        invokeDialogUnique(new LocalConfig(root));

    }//GEN-LAST:event_jMSettingsActionPerformed

    private void jMSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMSaveAsActionPerformed
        filesRepository.chooseFilesToSave().ifPresent(file ->
                new AutoMBox(this.getClass().getName(), () -> viewerPanel.exportFile(file.toPath()))
        );
    }//GEN-LAST:event_jMSaveAsActionPerformed

    private void jMOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMOpenFileActionPerformed
        filesRepository.chooseFilesToOpen().forEach(this::openFile);
    }//GEN-LAST:event_jMOpenFileActionPerformed

    private void jMPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMPrintActionPerformed
        if (printerJob.printDialog()) {
            new AutoMBox(this.getClass().getName(), printerJob::print);
        }
    }//GEN-LAST:event_jMPrintActionPerformed

    private void viewerPanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewerPanelPropertyChange
        if (ViewerPanel.FILE_NAME_PROPERTY.equals(evt.getPropertyName())) {
            String file_name = (String) evt.getNewValue();
            setTitle(root.MlM(root.getAppTitle()) + ": " + file_name);
            jMDetail.setEnabled(true);
            jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));
        }
    }//GEN-LAST:event_viewerPanelPropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMDetail;
    private javax.swing.JMenuItem jMNav;
    private javax.swing.JMenuBar menubar;
    private net.sourceforge.MSGViewer.ViewerPanel viewerPanel;
    // End of variables declaration//GEN-END:variables

    private void openMail(String file_name) {
        MainWin win = new MainWin(root);
        win.viewerPanel.view(file_name);

        if (!menubar.isVisible())
            win.hideMenuBar();

        invokeDialog(win);
    }

    @Override
    public void hideMenuBar() {
        menubar.setVisible(false);
    }
}
