package at.redeye.FrameWork.base.prm;

import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;

public interface PrmListener {

    void onChange(PrmDefaultChecksInterface defaultChecks,
                  PrmActionEvent prmActionEvent) ;

}
