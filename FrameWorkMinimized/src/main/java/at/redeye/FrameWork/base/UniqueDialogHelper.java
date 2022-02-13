package at.redeye.FrameWork.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple class that holds a list of open dialogs that should only be opened once
 * @author martin
 */
public class UniqueDialogHelper
{
    private final Map<String, BaseDialogBase> dialogs = new HashMap<>();

    public BaseDialogBase invokeUniqueDialog( final BaseDialogBase dialog )
    {
        BaseDialogBase d = dialogs.get(dialog.getUniqueDialogIdentifier());

        if( d == null )
        {
            dialogs.put(dialog.getUniqueDialogIdentifier(), dialog);

            dialog.registerOnCloseListener(() -> dialogs.remove(dialog.getUniqueDialogIdentifier()));

            return dialog;

        } else {
            dialog.close();
        }

        return d;
    }
}
