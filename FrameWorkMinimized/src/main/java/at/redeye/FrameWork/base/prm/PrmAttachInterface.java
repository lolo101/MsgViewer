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
public interface PrmAttachInterface {

    public void addPrmListener(PrmListener listener);

    public void removePrmListener(PrmListener listener);

    public void updateListeners(PrmActionEvent prmActionEvent);

}
