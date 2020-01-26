/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.tablemanipulator.TableManipulator;

/**
 *
 * @author martin
 */
public interface CanCloseInterface
{
    boolean isEdited();
    int checkSave();
    void saveData();
    int checkSave(TableManipulator tm);
}
