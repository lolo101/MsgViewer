package at.redeye.Plugins.ShellExec;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;
import at.redeye.FrameWork.widgets.helpwindow.HyperlinkExecuter;
import at.redeye.FrameWork.widgets.helpwindow.OpenUrlInterface;

public class Plugin implements at.redeye.FrameWork.Plugin.Plugin, OpenUrlInterface {
    private static final String NAME = "ShellExec";

    public String getName() {
        return NAME;
    }

    public String getLicenceText() {
        return new AutoMBox<>(Plugin.class.getSimpleName(), () -> {
            HelpFileLoader helper = new HelpFileLoader();
            return helper.loadHelp("/at/redeye/Plugins/ShellExec", "Licence");
        }).resultOrElse("");
    }

    public void initPlugin(Object obj) {
        if (obj instanceof Root) {
            Root root = (Root) obj;
            root.addDllExtractorToCache(new HSWChellExecDLL());
            if (Setup.is_win_system()) {
                HyperlinkExecuter.setOpenUrl(this);
            }

            ShellExec.init(obj.getClass());
        }
    }

    public boolean isAvailable() {
        return Setup.is_win_system();
    }

    public String toString() {
        return this.getName() + " " + this.getVersion();
    }

    public String getChangeLog() {
        return new AutoMBox<>(Plugin.class.getSimpleName(), () -> {
            HelpFileLoader helper = new HelpFileLoader();
            return helper.loadHelp("/at/redeye/Plugins/ShellExec", "ChangeLog");
        }).resultOrElse("");
    }

    public String getVersion() {
        return "1.1";
    }

    public void openUrl(String url) {
        ShellExec shell = new ShellExec();
        shell.execute(url);
    }
}
