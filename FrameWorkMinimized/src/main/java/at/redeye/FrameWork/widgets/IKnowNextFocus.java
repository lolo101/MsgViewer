/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import java.awt.Container;

/**
 *
 * @author martin
 */
public interface IKnowNextFocus
{
    Container get_next_on_focus();
    Container get_prev_on_focus();
    void set_next_on_focus( Container next );
    void set_prev_on_focus( Container prev );
}
