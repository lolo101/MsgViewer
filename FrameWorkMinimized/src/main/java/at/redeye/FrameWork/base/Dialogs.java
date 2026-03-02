package at.redeye.FrameWork.base;

import java.util.Collection;
import java.util.HashSet;

public class Dialogs {
    private final Collection<BaseDialogBase> dialogs = new HashSet<>();
    private final Root root;

    public Dialogs(Root root) {
        this.root = root;
    }

    public void informWindowOpened(BaseDialogBase dlg) {
        dialogs.add(dlg);
    }

    public void informWindowClosed(BaseDialogBase dlg) {
        dialogs.remove(dlg);

        if (dialogs.isEmpty()) {
            System.out.println("All Windows closed, normal exit");
            root.appExit();
        }
    }
}
