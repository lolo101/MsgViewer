package at.redeye.Plugins.ShellExec;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.Plugins;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;
import at.redeye.FrameWork.widgets.helpwindow.HyperlinkExecuter;
import at.redeye.FrameWork.widgets.helpwindow.OpenUrlInterface;

public class Plugin implements at.redeye.FrameWork.Plugin.Plugin, OpenUrlInterface {
    private static final String NAME = "ShellExec";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLicenceText() {
        return new AutoMBox<>(Plugin.class.getSimpleName(), () -> {
            HelpFileLoader helper = new HelpFileLoader();
            return helper.loadHelp("/at/redeye/Plugins/ShellExec", "Licence");
        }).resultOrElse("");
    }

    @Override
    public void initPlugin(Object obj) {
        if (obj instanceof Plugins plugins) {
            plugins.addDllExtractorToCache(new HSWChellExecDLL());
            if (Setup.is_win_system()) {
                HyperlinkExecuter.setOpenUrl(this);
            }

            ShellExec.init(obj.getClass());
        }
    }

    @Override
    public boolean isAvailable() {
        return Setup.is_win_system();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public String getChangeLog() {
        return new AutoMBox<>(Plugin.class.getSimpleName(), () -> {
            HelpFileLoader helper = new HelpFileLoader();
            return helper.loadHelp("/at/redeye/Plugins/ShellExec", "ChangeLog");
        }).resultOrElse("");
    }

    @Override
    public void openUrl(String url) {
        ShellExec shell = new ShellExec();
        shell.execute(url);
    }
}
