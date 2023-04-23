package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

public class DefaultCanClose {

    public static boolean DefaultCanCloseWithTable(Saveable iface, TableManipulator tm )
    {
        int ret = iface.checkSave(tm);

        if (ret == 1) {
            iface.saveData();
            return true;
        }
        return ret != -1;
    }
}
