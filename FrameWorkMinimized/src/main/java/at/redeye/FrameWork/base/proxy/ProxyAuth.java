/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProxyAuth.java
 *
 * Created on 26.07.2010, 14:51:04
 */

package at.redeye.FrameWork.base.proxy;

import at.redeye.FrameWork.base.BaseDialogDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.utilities.StringUtils;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 *
 * @author martin
 */
public class ProxyAuth extends BaseDialogDialog {

    public static final String SAVE_PASSWORD="ProxyAuth.SAVE_PASSWORD";

    boolean isOk = false;

    /** Creates new form ProxyAuth */
    public ProxyAuth(Root root) {
        super(root,"Proxy Authentifizierung");

        initComponents();
        initCommon();
    }

    public ProxyAuth(Root root, String saved_proxy_domain, String saved_proxy_user, String saved_proxy_pass) {
        super(root,"Proxy Authentifizierung");

        initComponents();

        jTDomain.setText(saved_proxy_domain);
        jTName.setText(saved_proxy_user);
        JPassword.setText(saved_proxy_pass);

        initCommon();
    }

    private void initCommon()
    {
        if (StringUtils.isYes(root.getSetup().getLocalConfig(SAVE_PASSWORD, "1")))
        {
            jCSavePasswd.setSelected(true);
        }

        registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), new Runnable() {

            @Override
            public void run() {
                jButtonOkActionPerformed(null);
            }
        });
    }

    public String getUserName()
    {
        return jTName.getText();
    }

    public String getPassword()
    {
        return new String(JPassword.getPassword());
    }

    public String getDomain()
    {
        return jTDomain.getText();
    }

    public boolean execOk()
    {
        return isOk;
    }

    public boolean savePassword()
    {
        return jCSavePasswd.isSelected();
    }

    @Override
    public void close()
    {
        root.getSetup().setLocalConfig(SAVE_PASSWORD, String.valueOf(jCSavePasswd.isSelected()));
        super.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTName = new javax.swing.JTextField();
        jTDomain = new javax.swing.JTextField();
        JPassword = new javax.swing.JPasswordField();
        jCSavePasswd = new javax.swing.JCheckBox();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Name");

        jLabel2.setText("Passwort");

        jLabel3.setText("Domäne");

        jCSavePasswd.setSelected(true);
        jCSavePasswd.setText("Kennwort Speichern");

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(this::jButtonOkActionPerformed);

        jButtonCancel.setText("Abbrechen");
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Proxy Authentifizierung");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonOk, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(jButtonCancel))
                    .addComponent(jCSavePasswd, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTDomain, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(JPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(jTName, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCSavePasswd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        isOk = true;

        closeNoAppExit();

    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed

        closeNoAppExit();

    }//GEN-LAST:event_jButtonCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField JPassword;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JCheckBox jCSavePasswd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTDomain;
    private javax.swing.JTextField jTName;
    // End of variables declaration//GEN-END:variables

}
