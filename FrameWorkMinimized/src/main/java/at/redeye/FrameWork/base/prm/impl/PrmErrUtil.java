package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.bindtypes.DBString;

import javax.swing.*;

public class PrmErrUtil {

    public static void displayPrmError(BaseDialog dlg, DBString prmName) {

        JOptionPane.showMessageDialog(dlg,
                "Der eingegebene Wert für Parameter >" + prmName + "< ist unzulässig!",
                "Parameter ändern", JOptionPane.ERROR_MESSAGE);
    }
}
