package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

public interface Saveable
{
    void saveData();
    int checkSave(TableManipulator tm);
}
