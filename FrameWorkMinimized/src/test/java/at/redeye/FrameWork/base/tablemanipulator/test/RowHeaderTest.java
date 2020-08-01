/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TableTest.java
 *
 * Created on 15.05.2010, 09:33:53
 */

package at.redeye.FrameWork.base.tablemanipulator.test;

import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

import javax.swing.*;
import java.util.Vector;

/**
 *
 * @author martin
 */
public class RowHeaderTest extends BaseDialog {

    /** Creates new form TableTest */
    int i;
    TableManipulator tm;

    public RowHeaderTest(Root root) {
        super(root,"Table RowHeader Test");

        initComponents();

        DBConfig config = new DBConfig();

        tm = new TableManipulator(root, table, config);

        tm.hide(config.hist.an_user);
        tm.hide(config.hist.an_zeit);
        tm.hide(config.hist.lo_user);
        tm.hide(config.hist.lo_zeit);

        tm.setEditable(config.name);

        Vector<DBConfig> data = new Vector<>();

        for( i = 0 ; i < 19; i++ )
        {
            DBConfig c = new DBConfig();

            c.name.loadFromString(String.valueOf(i));
            c.value.loadFromString("value " + i);
            c.descr.loadFromString("foobar " + i);
            c.hist.setAeHist("martin");

            data.add(c);
        }

        tm.prepareTable();

        tm.addAll(data);

        tm.autoResize();

        if( StyleName.equals("system") )
            jr_system.setSelected(true);
        else if( StyleName.equals("metal") )
            jr_metal.setSelected(true);
        else if( StyleName.equals("motif") )
            jr_motif.setSelected(true);
        else
            jr_nimbus.setSelected(true);

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jr_system = new javax.swing.JRadioButton();
        jr_motif = new javax.swing.JRadioButton();
        jr_nimbus = new javax.swing.JRadioButton();
        jr_metal = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCShowRowHeader = new javax.swing.JCheckBox();
        jlTest = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(table);

        jr_system.setText("System");
        jr_system.addActionListener(this::jr_systemActionPerformed);

        jr_motif.setText("Motif");
        jr_motif.addActionListener(this::jr_motifActionPerformed);

        jr_nimbus.setText("Nimbus");
        jr_nimbus.addActionListener(this::jr_nimbusActionPerformed);

        jr_metal.setText("Metal");
        jr_metal.addActionListener(this::jr_metalActionPerformed);

        jButton1.setText("New");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Del");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jCShowRowHeader.setSelected(true);
        jCShowRowHeader.setText("Show RowHeader");
        jCShowRowHeader.addActionListener(this::jCShowRowHeaderActionPerformed);

        jlTest.setText("enabled");
        jlTest.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(jlTest, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCShowRowHeader)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCShowRowHeader)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jlTest))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jr_system)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jr_motif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jr_nimbus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jr_metal)
                .addGap(255, 255, 255))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jr_system)
                    .addComponent(jr_motif)
                    .addComponent(jr_nimbus)
                    .addComponent(jr_metal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

            DBConfig c = new DBConfig();

            c.name.loadFromString(String.valueOf(i));
            c.value.loadFromString("value " + i);
            c.descr.loadFromString("foobar " + i);
            c.hist.setAeHist("martin");

            i++;

            tm.add(c);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int row = tm.getSelectedRow();

        System.out.println("removing row: " + row);

        if( row != -1 )
        {
            tm.remove(row);
        } else {
            row = table.getRowCount() - 1;

            if( row >= 0 )
              tm.remove(row);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCShowRowHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCShowRowHeaderActionPerformed

        tm.showRowHeader(jCShowRowHeader.isSelected());

        if( jCShowRowHeader.isSelected() )
            jlTest.setText(MlM("enabled"));
        else
            jlTest.setText(MlM("disabled"));

    }//GEN-LAST:event_jCShowRowHeaderActionPerformed

    private void jr_systemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_systemActionPerformed

        jr_motif.setSelected(false);
        jr_nimbus.setSelected(false);
        jr_metal.setSelected(false);

        changeStyle("system");
    }//GEN-LAST:event_jr_systemActionPerformed

    private void jr_motifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_motifActionPerformed

        jr_system.setSelected(false);
        jr_nimbus.setSelected(false);
        jr_metal.setSelected(false);

        changeStyle("motif");
    }//GEN-LAST:event_jr_motifActionPerformed

    private void jr_nimbusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_nimbusActionPerformed

        jr_motif.setSelected(false);
        jr_system.setSelected(false);
        jr_metal.setSelected(false);

        changeStyle("nimbus");
    }//GEN-LAST:event_jr_nimbusActionPerformed

    private void jr_metalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jr_metalActionPerformed

        jr_motif.setSelected(false);
        jr_nimbus.setSelected(false);
        jr_system.setSelected(false);

        changeStyle("metal");
    }//GEN-LAST:event_jr_metalActionPerformed

    void changeStyle( final String name )
    {
        StyleName = name;

        new AutoMBox(RowHeaderTest.class.getName())
        {
            @Override
            public void do_stuff() throws Exception {
                UIManager.setLookAndFeel(BaseModuleLauncher.getLookAndFeelStrByName(name));
                root.closeAllWindowsNoAppExit();
                run();
            }
        };
    }

    /**
    * @param args the command line RowHeaderTest
    */
    private static Root main_root;
    private static String StyleName = "metal";

    public static void main(String[] args) {

        main_root = new LocalRoot("TT");

        run();
    }

    private static void run()
    {
        new RowHeaderTest(main_root).setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCShowRowHeader;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlTest;
    private javax.swing.JRadioButton jr_metal;
    private javax.swing.JRadioButton jr_motif;
    private javax.swing.JRadioButton jr_nimbus;
    private javax.swing.JRadioButton jr_system;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

}
