package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.Plugin.AboutPlugins;
import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import at.redeye.FrameWork.utilities.StringUtils;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URLDecoder;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWin extends BaseDialog implements MainDialog, OpenNewMailInterface, LoadMessageInterface
{
    private String dialog_id;
    private final ViewerHelper helper;

    private static String last_path = null;

    /** Creates new form MainWin */
    public MainWin(Root root, final String file_name ) {
        super(root, file_name != null ? (root.MlM(root.getAppTitle()) + ": " + file_name) : root.getAppTitle() );

        helper = new ViewerHelper(root);

        initComponents();

        viewerPanel.setRoot(root, this);
        viewerPanel.setopenNewMailInterface(this);

        last_path = root.getSetup().getLocalConfig("LastPath","");

        if( file_name == null )
        {
            viewerPanel.getHeaderPane().setText(MlM("Drag a msg file into this window") );
        } else {
            EventQueue.invokeLater(() -> {
                jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));
                viewerPanel.parse(file_name);
            });
        }

        new EditorDropTarget(this,viewerPanel.getHeaderPane());
        new EditorDropTarget(this,viewerPanel.getBodyPane());

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_V,0), () -> jMDetailActionPerformed(null));

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_N,0), () -> {
            if( jMNav.isEnabled() )
                jMNavActionPerformed(null);
        });
    }

    @Override
    public String getUniqueDialogIdentifier(Object requester)
    {
        /*
         * This way the title of the dialog won't change the id, of the dialog.
         * The id of the dialog is used for saveing with height and position of
         * the dialog
         */
        if( dialog_id == null )
         dialog_id = super.getUniqueDialogIdentifier(requester);

        return dialog_id;
    }

    @Override
    public void close()
    {
        root.getSetup().setLocalConfig("LastPath", last_path);

        viewerPanel.dispose();
        helper.dispose();

        super.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewerPanel = new net.sourceforge.MSGViewer.ViewerPanel();
        menubar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMOpenFile = new javax.swing.JMenuItem();
        jMSaveAs = new javax.swing.JMenuItem();
        jMSettings = new javax.swing.JMenuItem();
        jMQuit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMDetail = new javax.swing.JMenuItem();
        jMNav = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMAbout = new javax.swing.JMenuItem();
        jMChangeLog = new javax.swing.JMenuItem();
        jMPlugin = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 400));

        jMenu1.setText("Program");

        jMOpenFile.setText("File Open ...");
        jMOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMOpenFileActionPerformed(evt);
            }
        });
        jMenu1.add(jMOpenFile);

        jMSaveAs.setText("Save File as ...");
        jMSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMSaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(jMSaveAs);

        jMSettings.setText("Options");
        jMSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMSettingsActionPerformed(evt);
            }
        });
        jMenu1.add(jMSettings);

        jMQuit.setText("Quit");
        jMQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMQuitActionPerformed(evt);
            }
        });
        jMenu1.add(jMQuit);

        menubar.add(jMenu1);

        jMenu2.setText("Info");

        jMDetail.setText("Details");
        jMDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMDetailActionPerformed(evt);
            }
        });
        jMenu2.add(jMDetail);

        jMNav.setText("MSG Navigator");
        jMNav.setEnabled(false);
        jMNav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMNavActionPerformed(evt);
            }
        });
        jMenu2.add(jMNav);
        jMenu2.add(jSeparator1);

        jMAbout.setText("About");
        jMAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMAboutActionPerformed(evt);
            }
        });
        jMenu2.add(jMAbout);

        jMChangeLog.setText("Changelog");
        jMChangeLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMChangeLogActionPerformed(evt);
            }
        });
        jMenu2.add(jMChangeLog);

        jMPlugin.setText("Plugins");
        jMPlugin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMPluginActionPerformed(evt);
            }
        });
        jMenu2.add(jMPlugin);

        menubar.add(jMenu2);

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

        if( viewerPanel.getMessage() != null)
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

        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        final FileFilter msg_filter = new FileNameExtensionFilter(MlM("Outlook *.msg Datei"), "msg");
        final FileFilter mbox_filter = new FileNameExtensionFilter(MlM("Unix *.mbox Datei"), "mbox");
        final FileFilter eml_filter = new FileNameExtensionFilter(MlM("Thunderbird *.eml Datei"), "eml");
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
    }//GEN-LAST:event_jMSaveAsActionPerformed

    private void jMOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMOpenFileActionPerformed

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
            loadMessage(file.getPath());
        }

    }//GEN-LAST:event_jMOpenFileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMAbout;
    private javax.swing.JMenuItem jMChangeLog;
    private javax.swing.JMenuItem jMDetail;
    private javax.swing.JMenuItem jMNav;
    private javax.swing.JMenuItem jMOpenFile;
    private javax.swing.JMenuItem jMPlugin;
    private javax.swing.JMenuItem jMQuit;
    private javax.swing.JMenuItem jMSaveAs;
    private javax.swing.JMenuItem jMSettings;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuBar menubar;
    private net.sourceforge.MSGViewer.ViewerPanel viewerPanel;
    // End of variables declaration//GEN-END:variables


    @Override
    public void openMail(Root root, String file_name) {
        MainWin win = new MainWin(root, file_name);

        if( !menubar.isVisible() )
            win.hideMenuBar();

        invokeDialog(win);
    }

    @Override
    public void loadMessage(String file_name)
    {
        logger.info("filename: " + file_name);

        if( file_name.startsWith("file://") )
        {
            try
            {
                file_name = URLDecoder.decode(file_name,"UTF-8");
                file_name = file_name.substring(7);

            } catch( UnsupportedEncodingException ex ) {
                logger.error(StringUtils.exceptionToString(ex));
                file_name = file_name.substring(7);
            }
        }

        jMNav.setEnabled(file_name.toLowerCase().endsWith(".msg"));

        if( viewerPanel.getMessage() == null )
        {
            viewerPanel.parse(file_name);
        }
        else
        {
            MainWin win = new MainWin( root, file_name );

            if( !menubar.isVisible() )
                win.hideMenuBar();

            invokeMainDialog( win );
        }
    }

    public String getLastOpenPath()
    {
        return last_path;
    }

    public void setLastOpenPath(String path)
    {
        last_path = path;
    }

    @Override
    public void hideMenuBar() {
       menubar.setVisible(false);
    }


    public ViewerHelper getHelper() {
        return helper;
    }
}
