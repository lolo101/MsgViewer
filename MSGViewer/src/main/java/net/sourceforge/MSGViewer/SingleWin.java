package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.Plugin.AboutPlugins;
import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class SingleWin extends BaseDialog implements MainDialog {

    private static String last_path = null;
    private String dialog_id;

    public SingleWin(Root root, final String file_name) {
        super(root, file_name != null ? (root.MlM(root.getAppTitle()) + ": " + file_name) : root.getAppTitle());
        initComponents();

        last_path = root.getSetup().getLocalConfig("LastPath", "");

        viewerPanel.setRoot(root, this);
        viewerPanel.setopenNewMailInterface(this::openMail);

        if (file_name == null) {
            viewerPanel.getHeaderPane().setText(MlM("Drag a msg file into this window"));
        } else {
            EventQueue.invokeLater(() -> {
                jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));
                viewerPanel.parse(file_name);
            });
        }


        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), () -> {
            if (jMNav.isEnabled())
                jMNavActionPerformed(null);
        });
    }

    @Override
    public String getUniqueDialogIdentifier(Object requester) {
        if (dialog_id == null)
            dialog_id = super.getUniqueDialogIdentifier(requester);

        return dialog_id;
    }

    @Override
    public void close() {
        if (last_path != null)
            root.getSetup().setLocalConfig("LastPath", last_path);


        super.close();
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
        javax.swing.JMenuItem jMDetail = new javax.swing.JMenuItem();
        jMNav = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem jMAbout = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMChangelog = new javax.swing.JMenuItem();
        javax.swing.JMenuItem jMPlugins = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new MSGFileFilter(root));
        fc.setMultiSelectionEnabled(true);

        logger.info("last path: " + last_path);
        if (last_path != null) {
            fc.setCurrentDirectory(new File(last_path));
        }
        int retval = fc.showOpenDialog(this);
        if (retval != 0) {
            return;
        }
        final File[] files = fc.getSelectedFiles();
        for (File file : files) {
            last_path = file.getPath();
            openMail(file.getPath());
        }
    }//GEN-LAST:event_jFileOpenActionPerformed

    private void jMFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMFileSaveActionPerformed

        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        final FileFilter msg_filter = new FileNameExtensionFilter(MlM("Outlook *.msg File"), "msg");
        final FileFilter mbox_filter = new FileNameExtensionFilter(MlM("Unix *.mbox File"), "mbox");
        final FileFilter eml_filter = new FileNameExtensionFilter(MlM("Thunderbird *.eml File"), "eml");
        fc.addChoosableFileFilter(msg_filter);
        fc.addChoosableFileFilter(mbox_filter);
        fc.addChoosableFileFilter(eml_filter);
        fc.setMultiSelectionEnabled(false);
        if (last_path != null) {
            fc.setCurrentDirectory(new File(last_path));
        }
        int retval = fc.showSaveDialog(this);
        if (retval != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final File file = fc.getSelectedFile();
        new AutoMBox(this.getClass().getName(), () -> {
            File export_file = file;
            last_path = file.getPath();
            if (!file.getName().toLowerCase().endsWith(".msg") && !file.getName().toLowerCase().endsWith(".eml") && !file.getName().toLowerCase().endsWith(".mbox")) {
                if (fc.getFileFilter() == msg_filter) {
                    export_file = new File(file.getAbsolutePath() + ".msg");
                } else if (fc.getFileFilter() == eml_filter) {
                    export_file = new File(file.getAbsolutePath() + ".eml");
                } else {
                    export_file = new File(file.getAbsolutePath() + ".mbox");
                }
            }
            viewerPanel.exportFile(export_file);
        });

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

        if (viewerPanel.getMessage() != null)
            invokeDialogUnique(new Internals(root, viewerPanel.getMessage()));

    }//GEN-LAST:event_jMDetailActionPerformed

    private void jMNavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMNavActionPerformed

        invokeDialogUnique(new MSGNavigator(root, new File(viewerPanel.getFileName())));

    }//GEN-LAST:event_jMNavActionPerformed

    private void openMail(String file) {
        SingleWin win = new SingleWin(root, file);

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
    private javax.swing.JMenuItem jMNav;
    private javax.swing.JMenuBar menubar;
    private net.sourceforge.MSGViewer.ViewerPanel viewerPanel;
    // End of variables declaration//GEN-END:variables

}
