package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;

import java.io.File;

public abstract class BaseWin extends BaseDialog implements MainDialog {
    private String dialog_id;

    public BaseWin(Root root) {
        super(root, root.MlM(root.getAppTitle()));
    }

    @Override
    public String getUniqueDialogIdentifier(Object requester) {
        if (dialog_id == null)
            dialog_id = super.getUniqueDialogIdentifier(requester);

        return dialog_id;
    }

    public abstract void openFile(File file);
}
