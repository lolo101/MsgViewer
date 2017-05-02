package net.sourceforge.MSGViewer.Plugins.poi;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;

/**
 *
 * @author martin
 */
public class Plugin implements at.redeye.FrameWork.Plugin.Plugin
{
    public static final String NAME = "Apache POI";

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
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

    @Override
    public void initPlugin(Object obj)
    {

    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String toString()
    {
        return getName() + " " + getVersion();
    }

    @Override
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

    @Override
    public String getVersion() {
        return Version.getVersion();
    }
}
