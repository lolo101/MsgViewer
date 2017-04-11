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
public class DefaultCanClose 
{
    public static boolean DefaultCanClose( CanCloseInterface iface )
    {
        if( iface.isEdited() == true )        
        {
            int ret = iface.checkSave();

            if (ret == 1) {
                iface.saveData();
            } else if (ret == -1) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean DefaultCanCloseWithTable( CanCloseInterface iface, TableManipulator tm )
    {
        int ret = iface.checkSave(tm);

        if (ret == 1) {
            iface.saveData();
        } else if (ret == -1) {
            return false;
        }        
        return true;
    }
}
