package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

public interface CanSaveInterface
{
    void saveData();
    int checkSave(TableManipulator tm);
}
