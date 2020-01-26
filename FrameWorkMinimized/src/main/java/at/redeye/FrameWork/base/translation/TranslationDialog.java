/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TranslationDialog.java
 *
 * Created on 17.04.2010, 23:40:54
 */

package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.widgets.GridLayout2;
import at.redeye.FrameWork.widgets.NoticeIfChangedTextField;
import at.redeye.FrameWork.widgets.helpwindow.HelpWin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

/**
 *
 * @author martin
 */
public class TranslationDialog extends BaseDialog {

    public static String TRANS_LAST_LANGUAGE = "last_language";
    public static String TRANS_LAST_COUNTRY = "last_country";
    public static String TRANS_LAST_LEFT_COLCOUNT = "trans_last_right_colcount";
    public static String TRANS_LAST_RIGHT_COLCOUNT = "trans_last_left_colcount";

    Vector<SimpleEntry<String,StringBuffer>> data = new Vector<>();
    Vector<NoticeIfChangedTextField> fields = new Vector<>();

    String ClassName;

    int prev_lang_index = -1;
    int prev_country_index = -1;

    boolean force_undo_country = false;
    boolean force_undo_language = false;

    Vector<JTextField> left_cols = new Vector<>();
    Vector<JTextField> right_cols = new Vector<>();

    public TranslationDialog(final Root root, Container frame , String name,  ExtractStrings es ) {
        super(root, name );
        initComponents();

        setBaseLanguage("de");

        ClassName = name;

        jLTitle.setText(getTitle(name));

        if( es == null )
            es = new ExtractStrings(frame);

        Set<String> strings = es.getStrings();

        panel.setLayout(new GridLayout2(0,5));


        for( String s : strings )
        {
            if( MLUtil.shouldBeTranslated(s))
            {
                final JTextField tf = new JTextField(s);

                tf.setEditable(false);
                tf.setFocusable(false);

                panel.add(tf);
                left_cols.add(tf);

                JButton copy = new JButton();
                copy.setBorderPainted(false);
                copy.setContentAreaFilled(false);
                copy.setMargin(new Insets(0, 0, 0, 0));
                copy.setToolTipText(MlM("Text kopieren"));

                copy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/next.png")));

                panel.add(copy);


                JButton edit = new JButton();
                edit.setBorderPainted(false);
                edit.setContentAreaFilled(false);
                edit.setMargin(new Insets(0, 0, 0, 0));
                edit.setToolTipText(MlM("Text mehrzeilig editieren"));

                edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/edit.png")));

                panel.add(edit);


                String res = root.MlM(s);

                JButton wizard = null;

                if( res.equals(s) )
                {
                    panel.add( new JLabel() );
                } else {
                    wizard = new JButton();
                    wizard.setBorderPainted(false);
                    wizard.setContentAreaFilled(false);
                    wizard.setMargin(new Insets(0, 0, 0, 0));
                    wizard.setToolTipText(res);
                    wizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/wizard.gif")));

                    panel.add(wizard);
                }

                final NoticeIfChangedTextField editField = new NoticeIfChangedTextField();
                right_cols.add(editField);

                copy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editField.setText(tf.getText());
                    }
                });

                if( wizard != null )
                {
                    wizard.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            editField.setText(root.MlM(tf.getText()));
                        }
                    });
                }

                editField.setColumns(40);

                final JFrame base = this;

                edit.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        final MultiLineInputDialog dialog = new MultiLineInputDialog(base, root);

                        final MultiLineInput mli = dialog.getMli();
                        mli.setSaveActionListener(new Runnable() {
                            public void run()
                            {
                                editField.setText(mli.getText());
                                dialog.dispose();
                            }
                        });

                        mli.setCloseActionListener(dialog::dispose);

                        mli.setText(tf.getText());

                        String editedText =  editField.getText().trim();
                        if( !editedText.isEmpty() )
                            mli.setTransText(editedText);

                        // Adjust dialogs placement on screen
                        Dimension dialog_dim = dialog.getPreferredSize();

                        Rectangle rect = base.getBounds();
                        rect.x += rect.width / 2;
                        rect.y += rect.height / 2;
                        rect.x -= dialog_dim.height / 2;
                        rect.y -= dialog_dim.width / 2;
                        rect.height = dialog_dim.height;
                        rect.width = dialog_dim.width;

                        dialog.setBounds(rect);
                        dialog.setVisible(true);
                        dialog.toFront();
                    }
                });


                panel.add( editField );

                SimpleEntry<String,StringBuffer> pair = new SimpleEntry<>(s, new StringBuffer());

                this.bindVar( editField, pair.getValue() );

                fields.add(editField);

                data.add( pair );
            }
        }

        Locale def_locale = Locale.getDefault();

        String last_language = root.getSetup().getLocalConfig(TRANS_LAST_LANGUAGE, def_locale.getLanguage() );
        String last_country = root.getSetup().getLocalConfig(TRANS_LAST_COUNTRY, def_locale.getCountry() );


        language.removeAllItems();

        Set<String> languages = new TreeSet<>();

        for( Locale l :  Locale.getAvailableLocales() )
        {
            languages.add(l.getLanguage());
        }

        for( String s : languages )
        {
            language.addItem(s);


            if( s.equals(last_language) )
            {
                language.setSelectedItem(s);
            }
        }

        country.removeAllItems();

        Set<String> countries = new TreeSet<>();


        for( Locale l :  Locale.getAvailableLocales() )
        {
            countries.add(l.getCountry());
        }

        for( String s : countries )
        {
            country.addItem(s);

            if( s.equals(last_country) )
            {
                country.setSelectedItem(s);
            }
        }

        String last_col_left = root.getSetup().getLocalConfig(TRANS_LAST_LEFT_COLCOUNT, "40");
        String last_col_right = root.getSetup().getLocalConfig(TRANS_LAST_RIGHT_COLCOUNT, "40");

        int left = 40;
        int right = 40;

        try {
            left = Integer.parseInt(last_col_left);
        } catch( NumberFormatException ex ) {
            logger.error("Invalid Number: " + last_col_left);
        }

        try {
            right = Integer.parseInt(last_col_right);
        } catch( NumberFormatException ex ) {
            logger.error("Invalid Number: " + last_col_right);
        }

        colsLeft.setValue(left);
        colsRight.setValue(right);

        adjustScrollingSpeed(jScrollPane1);

        registerHelpWin(new Runnable() {

            public void run() {
                invokeDialogUnique(new HelpWin(root, "/at/redeye/FrameWork/base/translation/resources/Help", "TranslationDialog"));
            }
        });
    }

    private String getTitle(String name )
    {
        int index = name.lastIndexOf('.');

        if( index > 0 )
            name = name.substring(index+1);

        return String.format(MlM("Übersetzungen von %s "),  name);
    }


    @Override
    public void close()
    {
        root.getSetup().setLocalConfig(TRANS_LAST_LANGUAGE, language.getSelectedItem().toString() );
        root.getSetup().setLocalConfig(TRANS_LAST_COUNTRY, country.getSelectedItem().toString() );
        root.getSetup().setLocalConfig(TRANS_LAST_LEFT_COLCOUNT, colsLeft.getValue().toString() );
        root.getSetup().setLocalConfig(TRANS_LAST_RIGHT_COLCOUNT, colsRight.getValue().toString() );

        super.close();
    }

    @Override
    public boolean canClose()
    {
        for( NoticeIfChangedTextField field : fields )
        {
            if( field.hasChanged() )
            {
                setEdited();
                break;
            }
        }

        if( isEdited() )
        {
            int ret = checkSave();

            logger.info("res: " + ret );

            switch( ret )
            {
                case  0:
                    return true;

                case -1:
                    return false;

                case  1:
                    jBSaveActionPerformed(null);
                    return true;
            }
        }

        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBClose = new javax.swing.JButton();
        jBSave = new javax.swing.JButton();
        jLTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        language = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        country = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        locale_string = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        colsLeft = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        colsRight = new javax.swing.JSpinner();
        jBHelp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jBClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif"))); // NOI18N
        jBClose.setText("Schließen");
        jBClose.addActionListener(this::jBCloseActionPerformed);

        jBSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/button_ok.gif"))); // NOI18N
        jBSave.setText("Speichern");
        jBSave.addActionListener(this::jBSaveActionPerformed);

        jLTitle.setFont(new java.awt.Font("Dialog", Font.BOLD, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Übersetzungen");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 956, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        language.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        language.addActionListener(this::languageActionPerformed);

        jLabel1.setText("Sprache:");

        jLabel2.setText("Ländervariante:");

        country.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        country.addActionListener(this::countryActionPerformed);

        jLabel3.setText("Lokalisierungskürzel:");

        locale_string.setEditable(false);

        jLabel4.setText("Spalten Links:");

        colsLeft.addChangeListener(this::colsLeftStateChanged);

        jLabel5.setText("Spalten Rechts:");

        colsRight.addChangeListener(this::colsRightStateChanged);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(language, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locale_string, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colsLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colsRight, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(language, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(locale_string, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(colsLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(colsRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/help.png"))); // NOI18N
        jBHelp.addActionListener(this::jBHelpActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jBSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 714, Short.MAX_VALUE)
                        .addComponent(jBClose))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 930, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBHelp)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBSave)
                            .addComponent(jBClose))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCloseActionPerformed

        new AutoMBox(getTitle()) {
            @Override
            public void do_stuff() {

                if (canClose()) {
                    close();
                }
            }
        };
    }//GEN-LAST:event_jBCloseActionPerformed

    private void jBSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSaveActionPerformed

        new AutoMBox(getTitle()) {

            @Override
            public void do_stuff() throws Exception {

                gui_to_var();

                String dir = getTranslationsDir(root);

                File directory = new File(dir);

                if( !directory.isDirectory() )
                    directory.mkdir();

                String file_name =  dir + "/" + ClassName + "_" + locale_string.getText() + ".properties";

                Properties props = new Properties();

                for( SimpleEntry<String,StringBuffer> pair : data )
                {
                    if( pair.getValue().length() > 0 )
                    {
                        props.setProperty(pair.getKey(), pair.getValue().toString());
                    }
                }

                FileOutputStream out = new FileOutputStream(file_name);
                props.store(out, "nix");
                out.close();

                for(NoticeIfChangedTextField field : fields )
                    field.setChanged(false);

                setEdited(false);
            }

        };
    }//GEN-LAST:event_jBSaveActionPerformed


    private void languageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageActionPerformed

        if( force_undo_language || canClose() )
        {
            prev_lang_index = language.getSelectedIndex();
            force_undo_language = false;
            updateLocale();
        }
        else
        {
            if (prev_lang_index != -1)
            {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        force_undo_language = true;
                        language.setSelectedIndex(prev_lang_index);
                    }
                });
            }
        }
    }//GEN-LAST:event_languageActionPerformed

    private void countryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryActionPerformed

        if( force_undo_country || canClose() )
        {
            prev_country_index = country.getSelectedIndex();
            force_undo_country = false;
            updateLocale();
        }
        else
        {
            if( prev_country_index != -1 )
            {
                java.awt.EventQueue.invokeLater( new Runnable(){

                @Override
                public void run()
                {
                    force_undo_country = true;
                    country.setSelectedIndex(prev_country_index);
                }
                });
            }
        }

    }//GEN-LAST:event_countryActionPerformed

    private void colsLeftStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_colsLeftStateChanged
        Integer val = (Integer) colsLeft.getValue();
        for( JTextField field : left_cols )
            field.setColumns(val);
        panel.updateUI();
    }//GEN-LAST:event_colsLeftStateChanged

    private void colsRightStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_colsRightStateChanged
        Integer val = (Integer) colsRight.getValue();
        for( JTextField field : right_cols )
            field.setColumns(val);

        panel.updateUI();
    }//GEN-LAST:event_colsRightStateChanged

    private void jBHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBHelpActionPerformed

        callHelpWin();
    }//GEN-LAST:event_jBHelpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner colsLeft;
    private javax.swing.JSpinner colsRight;
    private javax.swing.JComboBox country;
    private javax.swing.JButton jBClose;
    private javax.swing.JButton jBHelp;
    private javax.swing.JButton jBSave;
    private javax.swing.JLabel jLTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox language;
    private javax.swing.JTextField locale_string;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

    private void updateLocale()
    {
        String lang = (String) language.getSelectedItem();
        String countr = (String) country.getSelectedItem();

        if( countr != null &&
            !countr.trim().isEmpty() )
        {
            lang += "_" + countr;
        }

        locale_string.setText(lang);

        new AutoMBox(TranslationDialog.class.getName()) {

            @Override
            public void do_stuff() throws Exception {
                loadTranslationsFor( ClassName, locale_string.getText() );
            }
        };

    }

    public static String getTranslationsDir( Root root )
    {
        String dir = Setup.getAppConfigDir(root.getAppName()) + "/translations/";

        File fdir = new File( dir );

        if( !fdir.exists() )
            fdir.mkdirs();

        return dir;
    }

    private void loadTranslationsFor(String ClassName, String lang) throws FileNotFoundException, IOException
    {
         String dir = getTranslationsDir(root);

         File file = new File( dir + "/" + ClassName + "_" + lang + ".properties");

         Properties props = new Properties();

         if( file.isFile() )
         {
            FileInputStream in = new FileInputStream(file);
            props.load(in);
            in.close();
         }

         for(SimpleEntry<String,StringBuffer> p : data )
         {
             String trans = props.getProperty(p.getKey());

             StringBuffer buf = p.getValue();
             buf.setLength(0);

             if( trans != null )
             {
                 buf.append(trans);
             }
         }

         var_to_gui();

        for (NoticeIfChangedTextField field : fields) {
            field.setChanged(false);
        }

        setEdited(false);
    }

}
