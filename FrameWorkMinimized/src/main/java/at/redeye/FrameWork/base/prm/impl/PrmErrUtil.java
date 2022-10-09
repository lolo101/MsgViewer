package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.prm.impl.gui.GlobalConfig;

import javax.swing.*;

public class PrmErrUtil {

    public static void displayPrmError(BaseDialog dlg, String prmName) {

        JOptionPane.showMessageDialog(dlg,
                "Der eingegebene Wert für Parameter >" + prmName + "< ist unzulässig!",
                "Parameter ändern", JOptionPane.ERROR_MESSAGE);
    }

    public static void restoreGlobalPrm(final GlobalConfig dlg, String name, String saveValue) {

        dlg.getRoot().getSetup().setConfig(name, saveValue);
        SwingUtilities.invokeLater(() -> {
            dlg.getRoot().saveSetup();
            dlg.feed_table(true);
        });

    }
}
