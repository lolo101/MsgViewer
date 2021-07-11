package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

public class DefaultCanClose {

    public static boolean DefaultCanCloseWithTable(CanSaveInterface iface, TableManipulator tm )
    {
        int ret = iface.checkSave(tm);

        if (ret == 1) {
            iface.saveData();
            return true;
        } else return ret != -1;
    }
}
