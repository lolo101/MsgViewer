/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets.helpwindow;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class HyperlinkExecuter implements HyperlinkListener {

    private static final Logger logger = LogManager.getLogger(HyperlinkExecuter.class);
    private Root root;
    private static OpenUrlInterface open_url = null;

    public HyperlinkExecuter( Root root )
    {
        this.root = root;
    }

    @Override
    public void hyperlinkUpdate( final HyperlinkEvent e )
    {
        new AutoMBox(HelpWin.class.getName()) {

            @Override
            public void do_stuff() throws Exception {
                 hyperlinkUpdate_int(e);
            }
        };
    }

    public void hyperlinkUpdate_int(HyperlinkEvent e) throws IOException
    {
       if( !e.getEventType().equals(EventType.ACTIVATED) )
           return;

        logger.info(e.getURL());

        if( open_url != null )
        {
            open_url.openUrl(e.getURL().toString());
        }
        else
        {
            String open_command = getOpenCommand();

            String command = open_command + " \"" + e.getURL().toString() + "\"";
            logger.info(command);

            String[] command_array = new String[2];

            command_array[0] = open_command;
            command_array[1] = e.getURL().toString();

            Process p = Runtime.getRuntime().exec(command_array);
        }

    }

    public String getOpenCommand()
    {
        /*
        if( Setup.is_win_system() )
            return "explorer";
        */

        return root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.OpenCommand);
    }

    public static void setOpenUrl( OpenUrlInterface open_url_ )
    {
        open_url = open_url_;
    }
}
