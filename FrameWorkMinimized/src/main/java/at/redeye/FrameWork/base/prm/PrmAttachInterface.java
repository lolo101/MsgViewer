package at.redeye.FrameWork.base.prm;

import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;

public interface PrmAttachInterface {

    void addPrmListener(PrmListener listener);

    void removePrmListener(PrmListener listener);

    void updateListeners(PrmActionEvent prmActionEvent);

}
