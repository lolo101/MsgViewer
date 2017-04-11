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

    public final static long PRM_IS_LONG            = 0x0001;
    public final static long PRM_IS_DOUBLE          = 0x0002;
    public final static long PRM_IS_BIT             = 0x0004;
    public final static long PRM_IS_TRUE_FALSE      = 0x0008;
    public final static long PRM_HAS_VALUE          = 0x0010;
    public final static long PRM_IS_TIME            = 0x0020;
    public final static long PRM_IS_SHORTTIME       = 0x0040;
    public final static long PRM_IS_DATE            = 0x0080;
    public final static long PRM_IS_DATETIME        = 0x0100;
    public final static long PRM_IS_LOOKANDFEEL 	= 0x0200;

    public boolean doChecks (PrmActionEvent event);

}
