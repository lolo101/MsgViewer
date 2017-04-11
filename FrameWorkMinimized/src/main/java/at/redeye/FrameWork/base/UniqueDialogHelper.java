/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import java.util.HashMap;

/**
 * Simple class that holds a list of open dialogs that should only be opened once
 * @author martin
 */
public class UniqueDialogHelper
{
    HashMap<String,BaseDialogBase> dialogs = new HashMap<String,BaseDialogBase>();

    public static final String ID_STRING = "invokeDialogUnique";

    public BaseDialogBase invokeUniqueDialog( final BaseDialogBase dialog )
    {
        BaseDialogBase d = dialogs.get(dialog.getUniqueDialogIdentifier(ID_STRING));

        if( d == null )
        {
            dialogs.put(dialog.getUniqueDialogIdentifier(ID_STRING), dialog);

            dialog.registerOnCloseListener(new Runnable() {

                public void run() {
                    dialogs.remove(dialog.getUniqueDialogIdentifier(ID_STRING));
                }
            });

            return dialog;

        } else {
            dialog.close();
        }

        return d;
    }
}
