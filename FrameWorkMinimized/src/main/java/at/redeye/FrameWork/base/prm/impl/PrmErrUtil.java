/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.prm.impl;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.prm.impl.gui.GlobalConfig;

/**
 *
 * @author mmattl
 */
public class PrmErrUtil {

    public static void displayPrmError(BaseDialog dlg, String prmName) {

        JOptionPane.showMessageDialog(dlg,
                "Der eingegebene Wert für Parameter >" + prmName + "< ist unzulässig!",
                "Parameter ändern", JOptionPane.ERROR_MESSAGE);


    }

    public static void restoreGlobalPrm(final GlobalConfig dlg, String name, String saveValue) {

        dlg.getRoot().getSetup().setConfig(name, saveValue);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                dlg.getRoot().saveSetup();
                dlg.feed_table(true);
                
            }
        });
       
    }
}
