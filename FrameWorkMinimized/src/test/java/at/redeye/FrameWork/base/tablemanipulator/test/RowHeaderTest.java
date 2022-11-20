package at.redeye.FrameWork.base.tablemanipulator.test;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Vector;

public class RowHeaderTest extends BaseDialog {

    private int i;
    private final TableManipulator tm;

    private RowHeaderTest(Root root) {
        super(root, "Table RowHeader Test");

        initComponents();
        for (UIManager.LookAndFeelInfo installedLookAndFeel : UIManager.getInstalledLookAndFeels()) {
            String lookAndFeelName = installedLookAndFeel.getName();
            JRadioButton button = new JRadioButton(lookAndFeelName);
            button.setSelected(UIManager.getLookAndFeel().getName().equals(lookAndFeelName));
            button.addActionListener(evt -> changeStyle(evt, installedLookAndFeel.getClassName()));
            buttonGroupLaF.add(button);
            jPanelLookAndFeels.add(button);
        }

        DBConfig config = new DBConfig();

        tm = new TableManipulator(root, table, config);

        tm.hide(config.hist.an_user);
        tm.hide(config.hist.an_zeit);
        tm.hide(config.hist.lo_user);
        tm.hide(config.hist.lo_zeit);

        tm.setEditable(config.name);

        Collection<DBConfig> data = new Vector<>();

        for (i = 0; i < 19; i++) {
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
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupLaF = new javax.swing.ButtonGroup();
        jPanelLookAndFeels = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JButton jButton1 = new javax.swing.JButton();
        javax.swing.JButton jButton2 = new javax.swing.JButton();
        jCShowRowHeader = new javax.swing.JCheckBox();
        jlTest = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelLookAndFeels.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));
        getContentPane().add(jPanelLookAndFeels, java.awt.BorderLayout.PAGE_START);

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(table);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

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

        if (row != -1) {
            tm.remove(row);
        } else {
            row = table.getRowCount() - 1;

            if (row >= 0)
                tm.remove(row);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCShowRowHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCShowRowHeaderActionPerformed

        tm.showRowHeader(jCShowRowHeader.isSelected());

        if (jCShowRowHeader.isSelected())
            jlTest.setText(MlM("enabled"));
        else
            jlTest.setText(MlM("disabled"));

    }//GEN-LAST:event_jCShowRowHeaderActionPerformed

    private void changeStyle(ActionEvent evt, String className) {
        AbstractButton button = (AbstractButton) evt.getSource();
        button.setSelected(true);
        changeStyle(className);
    }

    private void changeStyle(final String className) {
        new AutoMBox<>(RowHeaderTest.class.getName(), () -> {
            UIManager.setLookAndFeel(className);
            root.closeAllWindowsNoAppExit();
            run();
        }).run();
    }

    private static Root main_root;

    // FIXME make me a JUnit test
    public static void main(String[] args) {

        main_root = new Root("TT");

        run();
    }

    private static void run() {
        new RowHeaderTest(main_root).setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupLaF;
    private javax.swing.JCheckBox jCShowRowHeader;
    private javax.swing.JPanel jPanelLookAndFeels;
    private javax.swing.JLabel jlTest;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

}
