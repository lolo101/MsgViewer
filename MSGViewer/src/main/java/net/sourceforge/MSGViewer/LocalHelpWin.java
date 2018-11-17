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
        super( root, "/Help/", Module );
    }
}
