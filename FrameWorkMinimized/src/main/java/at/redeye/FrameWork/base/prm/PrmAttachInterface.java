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

    void addPrmListener(PrmListener listener);

    void removePrmListener(PrmListener listener);

    void updateListeners(PrmActionEvent prmActionEvent);

}
