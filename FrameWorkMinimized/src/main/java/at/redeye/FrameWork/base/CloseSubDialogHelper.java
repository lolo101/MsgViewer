/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

/**
 *
 * @author martin
 */
public class CloseSubDialogHelper
{
    BaseDialogBase parent;

    CloseSubDialogHelper(BaseDialogBase parent)
    {
        this.parent = parent;
    }

    public void closeSubDialog( final BaseDialogBase dialog )
    {
        parent.registerOnCloseListener(new Runnable() {

            public void run() {
                dialog.close();
            }
        });
    }
}
