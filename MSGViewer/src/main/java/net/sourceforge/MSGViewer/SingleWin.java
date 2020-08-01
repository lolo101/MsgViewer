/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.Plugin.AboutPlugins;
import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import static at.redeye.FrameWork.base.BaseDialog.logger;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import at.redeye.FrameWork.utilities.StringUtils;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator;

/**
 *
 * @author martin
 */
public class SingleWin extends BaseDialog implements MainDialog, OpenNewMailInterface, LoadMessageInterface {

    private static String last_path = null;
    private String dialog_id;

    /**
     * Creates new form SingleWin
     */
    public SingleWin(Root root, final String file_name )
    {
        super(root, file_name != null ? (root.MlM(root.getAppTitle()) + ": " + file_name) : root.getAppTitle() );
        initComponents();

        last_path = root.getSetup().getLocalConfig("LastPath","");

        viewerPanel.setRoot(root, this);
        viewerPanel.setopenNewMailInterface(this);

        if( file_name == null )
        {
            viewerPanel.getHeaderPane().setText(MlM("Drag a msg file into this window") );
        } else {
            EventQueue.invokeLater(() -> {
                jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));
                viewerPanel.parse(file_name);
            });
        }


        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_N,0), new Runnable() {

            @Override
            public void run() {
                if( jMNav.isEnabled() )
                    jMNavActionPerformed(null);
            }
        });

        new EditorDropTarget(this,viewerPanel.getHeaderPane());
        new EditorDropTarget(this,viewerPanel.getBodyPane());
    }

    @Override
    public String getUniqueDialogIdentifier(Object requester)
    {
        /*
         * dadurch können wir später den Titel ändern, ohne das sich dadurch
         * die Dialog ID verändert.
         */
        if( dialog_id == null )
         dialog_id = super.getUniqueDialogIdentifier(requester);

        return dialog_id;
    }

    void cleanUp()
    {

    }

    @Override
    public void close()
    {
        cleanUp();

        if( last_path != null )
            root.getSetup().setLocalConfig("LastPath", last_path);


        super.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewerPanel = new net.sourceforge.MSGViewer.ViewerPanel();
        menubar = new javax.swing.JMenuBar();
        jMFileOpen = new javax.swing.JMenu();
        jFileOpen = new javax.swing.JMenuItem();
        jMFileSave = new javax.swing.JMenuItem();
        jMOptions = new javax.swing.JMenuItem();
        jMQuit = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMDetail = new javax.swing.JMenuItem();
        jMNav = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();

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

        jMenu4.setText("Info");

        jMDetail.setText("Details");
        jMDetail.addActionListener(this::jMDetailActionPerformed);
        jMenu4.add(jMDetail);

        jMNav.setText("MSG Navigator");
        jMNav.setEnabled(false);
        jMNav.addActionListener(this::jMNavActionPerformed);
        jMenu4.add(jMNav);
        jMenu4.add(jSeparator1);

        jMenuItem7.setText("About");
        jMenuItem7.addActionListener(this::jMenuItem7ActionPerformed);
        jMenu4.add(jMenuItem7);

        jMenuItem8.setText("Changelog");
        jMenuItem8.addActionListener(this::jMenuItem8ActionPerformed);
        jMenu4.add(jMenuItem8);

        jMenuItem9.setText("Plugins");
        jMenuItem9.addActionListener(this::jMenuItem9ActionPerformed);
        jMenu4.add(jMenuItem9);

        menubar.add(jMenu4);

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
            loadMessage(file.getPath());
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
        if (retval != 0) {
            return;
        }
        final File file = fc.getSelectedFile();
        new AutoMBox(this.getClass().getName()) {
            @Override
            public void do_stuff() throws Exception {
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
            }
        };

    }//GEN-LAST:event_jMFileSaveActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        invokeDialogUnique(new About(root));

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        invokeDialogUnique(new LocalHelpWin(root, "ChangeLog"));

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        invokeDialogUnique(new AboutPlugins(root));

    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMDetailActionPerformed

        if( viewerPanel.getMessage() != null)
             invokeDialogUnique(new Internals(root, viewerPanel.getMessage()));

    }//GEN-LAST:event_jMDetailActionPerformed

    private void jMNavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMNavActionPerformed

        invokeDialogUnique(new MSGNavigator(root, new File(viewerPanel.getFileName())));

    }//GEN-LAST:event_jMNavActionPerformed


    @Override
    public void loadMessage(String file_name)
    {
        logger.info("filename: " + file_name);

        if( file_name.startsWith("file://") )
        {
            file_name = URLDecoder.decode(file_name, StandardCharsets.UTF_8);
            file_name = file_name.substring(7);

        }

        jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));

        if( viewerPanel.getMessage() == null )
        {
            viewerPanel.parse(file_name);
        }
        else
        {
            SingleWin win = new SingleWin( root, file_name );

            if( !menubar.isVisible() )
                win.hideMenuBar();

            invokeMainDialog( win );
        }
    }

    @Override
    public void openMail(Root root, String file)
    {
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
    private javax.swing.JMenuItem jFileOpen;
    private javax.swing.JMenuItem jMDetail;
    private javax.swing.JMenu jMFileOpen;
    private javax.swing.JMenuItem jMFileSave;
    private javax.swing.JMenuItem jMNav;
    private javax.swing.JMenuItem jMOptions;
    private javax.swing.JMenuItem jMQuit;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuBar menubar;
    private net.sourceforge.MSGViewer.ViewerPanel viewerPanel;
    // End of variables declaration//GEN-END:variables

}
