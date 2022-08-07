package net.sourceforge.MSGViewer.Plugins.javamail;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;

public class Plugin implements at.redeye.FrameWork.Plugin.Plugin {
    public static final String NAME = "javamail";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLicenceText() {
        return new AutoMBox<>(Plugin.class.getSimpleName(),
                () -> new HelpFileLoader().loadHelp("/net/sourceforge/MSGViewer/Plugins/javamail", "oracle")
        ).resultOrElse("");
    }

    @Override
    public void initPlugin(Object obj) {

    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String toString() {
        return getName() + " " + getVersion();
    }

    @Override
    public String getChangeLog() {
        return new AutoMBox<>(Plugin.class.getSimpleName(),
                () -> new HelpFileLoader().loadHelp("/net/sourceforge/MSGViewer/Plugins/javamail", "ChangeLog")
        ).resultOrElse("");
    }

    @Override
    public String getVersion() {
        return Version.getVersion();
    }
}
