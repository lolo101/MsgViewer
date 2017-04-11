/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.widgets.helpwindow.HelpWin;

/**
 *
 * @author martin
 */
public class LocalHelpWin extends HelpWin {

    public LocalHelpWin( Root root, String Module )
    {
        super( root, "/net/sourceforge/MSGViewer/resources/Help/", Module );
    }
}
