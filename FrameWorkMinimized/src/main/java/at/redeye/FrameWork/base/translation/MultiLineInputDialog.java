/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MultiLineInputDialog.java
 *
 * Created on 20.09.2010, 14:38:13
 */

package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.BaseDialogDialog;
import at.redeye.FrameWork.base.Root;
import javax.swing.JFrame;

/**
 *
 * @author martin
 */
public class MultiLineInputDialog extends BaseDialogDialog {

    /** Creates new form MultiLineInputDialog */
    public MultiLineInputDialog(JFrame base, Root root) {
        super( base, root, "Text mehrzeilig editieren");
        initComponents();
        setBaseLanguage("de");

        
    }

    public MultiLineInput getMli()
    {
        return mli;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mli = new at.redeye.FrameWork.base.translation.MultiLineInput();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private at.redeye.FrameWork.base.translation.MultiLineInput mli;
    // End of variables declaration//GEN-END:variables

}
