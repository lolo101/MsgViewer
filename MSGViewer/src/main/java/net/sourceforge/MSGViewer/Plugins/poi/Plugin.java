/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer.Plugins.poi;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;

/**
 *
 * @author martin
 */
public class Plugin implements at.redeye.FrameWork.Plugin.Plugin
{    
    public static String NAME = "Apache POI";
    
    Root root;

    public String getName()
    {
        return NAME;
    }

    public String getLicenceText()
    {
        final StringBuilder builder = new StringBuilder();

        new AutoMBox( Plugin.class.getSimpleName() )
        {
            @Override
            public void do_stuff() throws Exception {
                HelpFileLoader helper = new HelpFileLoader();

                String licence = helper.loadHelp("/net/sourceforge/MSGViewer/Plugins/poi", "Apache");
                builder.append(licence);
            }
        };
        

        return builder.toString();
    }

    public void initPlugin(Object obj) 
    {

    }

    public boolean isAvailable() {
        return true;
    }

    @Override
    public String toString()
    {
        return getName() + " " + getVersion();
    }

    public String getChangeLog() {

        final StringBuilder builder = new StringBuilder();

        new AutoMBox( Plugin.class.getSimpleName() )
        {
            @Override
            public void do_stuff() throws Exception {
                HelpFileLoader helper = new HelpFileLoader();

                String changelog = helper.loadHelp("/net/sourceforge/MSGViewer/Plugins/poi", "ChangeLog");
                builder.append(changelog);
            }
        };


        return builder.toString();
    }

    public String getVersion() {
        return Version.getVersion();
    }
}
