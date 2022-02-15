package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.Plugin.AboutPlugins;
import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class SingleWin extends BaseWin {

    public SingleWin(Root root) {
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
        menubar = new javax.swing.JMenuBar();
        javax.swing.JMenu jMFileOpen = new javax.swing.JMenu();
        javax.swing.JMenuItem jFileOpen = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMFileSave = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMOptions = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMQuit = new javax.swing.JMenuItem();
        javax.swing.JMenu jMInfo = new javax.swing.JMenu();
        jMDetail = new javax.swing.JMenuItem();
        jMNav = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem jMAbout = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMChangelog = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMPlugins = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        viewerPanel.addPropertyChangeListener(this::viewerPanelPropertyChange);

        jMFileOpen.setText("Program");

        jFileOpen.setText("File Open ...");
        jFileOpen.addActionListener(this::jFileOpenActionPerformed);
        jMFileOpen.add(jFileOpen);

        jMFileSave.setText("Save File as ...");
        jMFileSave.addActionListener(this::jMFileSaveActionPerformed);
        jMFileOpen.add(jMFileSave);

        jMOptions.setText("Options");
        jMOptions.addActionListener(this::jMOptionsActionPerformed);
        jMFileOpen.add(jMOptions);

        jMQuit.setText("Quit");
        jMQuit.addActionListener(this::jMQuitActionPerformed);
        jMFileOpen.add(jMQuit);

        menubar.add(jMFileOpen);

        jMInfo.setText("Info");

        jMDetail.setText("Details");
        jMDetail.setEnabled(false);
        jMDetail.addActionListener(this::jMDetailActionPerformed);
        jMInfo.add(jMDetail);

        jMNav.setText("MSG Navigator");
        jMNav.setEnabled(false);
        jMNav.addActionListener(this::jMNavActionPerformed);
        jMInfo.add(jMNav);
        jMInfo.add(jSeparator1);

        jMAbout.setText("About");
        jMAbout.addActionListener(this::jMAboutActionPerformed);
        jMInfo.add(jMAbout);

        jMChangelog.setText("Changelog");
        jMChangelog.addActionListener(this::jMChangelogActionPerformed);
        jMInfo.add(jMChangelog);

        jMPlugins.setText("Plugins");
        jMPlugins.addActionListener(this::jMPluginsActionPerformed);
        jMInfo.add(jMPlugins);

        menubar.add(jMInfo);

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

    private void jMQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMQuitActionPerformed

        close();

    }//GEN-LAST:event_jMQuitActionPerformed

    private void jMOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMOptionsActionPerformed
        invokeDialogUnique(new LocalConfig(root));
    }//GEN-LAST:event_jMOptionsActionPerformed

    private void jFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileOpenActionPerformed
        filesRepository.chooseFilesToOpen().forEach(this::openFile);
    }//GEN-LAST:event_jFileOpenActionPerformed

    private void jMFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMFileSaveActionPerformed
        filesRepository.chooseFilesToSave().ifPresent(file ->
                new AutoMBox(this.getClass().getName(), () -> viewerPanel.exportFile(file))
        );
    }//GEN-LAST:event_jMFileSaveActionPerformed

    private void jMAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMAboutActionPerformed

        invokeDialogUnique(new About(root));

    }//GEN-LAST:event_jMAboutActionPerformed

    private void jMChangelogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMChangelogActionPerformed

        invokeDialogUnique(new LocalHelpWin(root, "ChangeLog"));

    }//GEN-LAST:event_jMChangelogActionPerformed

    private void jMPluginsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMPluginsActionPerformed

        invokeDialogUnique(new AboutPlugins(root));

    }//GEN-LAST:event_jMPluginsActionPerformed

    private void jMDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMDetailActionPerformed

        invokeDialogUnique(new Internals(root, viewerPanel.getMessage()));

    }//GEN-LAST:event_jMDetailActionPerformed

    private void jMNavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMNavActionPerformed

        invokeDialogUnique(new MSGNavigator(root, new File(viewerPanel.getFileName())));

    }//GEN-LAST:event_jMNavActionPerformed

    private void viewerPanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewerPanelPropertyChange
        if (ViewerPanel.FILE_NAME_PROPERTY.equals(evt.getPropertyName())) {
            String file_name = (String) evt.getNewValue();
            setTitle(root.MlM(root.getAppTitle()) + ": " + file_name);
            jMDetail.setEnabled(true);
            jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));
        }
    }//GEN-LAST:event_viewerPanelPropertyChange

    private void openMail(String file_name) {
        SingleWin win = new SingleWin(root);
        win.viewerPanel.view(file_name);

        if (!menubar.isVisible()) {
            win.hideMenuBar();
        }

        invokeDialog(win);
    }

    @Override
    public void hideMenuBar() {
        menubar.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMDetail;
    private javax.swing.JMenuItem jMNav;
    private javax.swing.JMenuBar menubar;
    private net.sourceforge.MSGViewer.ViewerPanel viewerPanel;
    // End of variables declaration//GEN-END:variables

}
