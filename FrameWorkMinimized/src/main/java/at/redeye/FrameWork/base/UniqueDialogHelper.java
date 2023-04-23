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
        String identifier = dialog.getUniqueDialogIdentifier();
        BaseDialogBase d = dialogs.get(identifier);

        if (d == null) {
            dialogs.put(identifier, dialog);

            dialog.registerOnCloseListener(() -> dialogs.remove(identifier));

            return dialog;

        }
        dialog.close();

        return d;
    }
}
