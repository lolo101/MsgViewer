/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.prm;

import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;

/**
 *
 * @author Mario
 */
public interface PrmListener {

    public void onChange (PrmDefaultChecksInterface defaultChecks,
            PrmActionEvent prmActionEvent) ;

    public void onChange (PrmCustomChecksInterface customChecks, PrmActionEvent event);

}
