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
public interface PrmDefaultChecksInterface {

    long PRM_IS_LONG            = 0x0001;
    long PRM_IS_DOUBLE          = 0x0002;
    long PRM_IS_BIT             = 0x0004;
    long PRM_IS_TRUE_FALSE      = 0x0008;
    long PRM_HAS_VALUE          = 0x0010;
    long PRM_IS_TIME            = 0x0020;
    long PRM_IS_SHORTTIME       = 0x0040;
    long PRM_IS_DATE            = 0x0080;
    long PRM_IS_DATETIME        = 0x0100;
    long PRM_IS_LOOKANDFEEL 	= 0x0200;

    boolean doChecks(PrmActionEvent event);

}
