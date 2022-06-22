package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.LocalRoot;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.widgets.StartupWindow;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.Arrays;

public class ModuleLauncher extends BaseModuleLauncher {

    private ModuleLauncher(String[] args) {
        super(args);

        BaseConfigureLogging(Level.ERROR);

        root = new LocalRoot("MSGViewer", "MSGViewer", false);
        root.setBaseLanguage("en");
        root.setDefaultLanguage("en");
        root.setLanguageTranslationResourcePath("/net/sourceforge/MSGViewer/resources/translations");
    }

    public static void main(String[] args) {
        new ModuleLauncher(args).invoke();
    }

    public void invoke() {
        if (getStartupFlag(CLIHelpMSGViewer.CLI_HELP)) {
            CLIHelpMSGViewer help = new CLIHelpMSGViewer(this);
            help.printHelpScreen();
            return;
        }

        if (getStartupFlag(CLIHelpMSGViewer.CLI_VERSION)) {
            CLIHelpMSGViewer help = new CLIHelpMSGViewer(this);
            help.printVersion();

            System.out.println("Copyright (C) 2015  Martin Oberzalek <msgviewer@hoffer.cx>\n" +
                    "\n" +
                    "This program is free software; you can redistribute it and/or modify\n" +
                    "it under the terms of the GNU General Public License as published by\n" +
                    "the Free Software Foundation; either version 3 of the License, or\n" +
                    "(at your option) any later version.   \n\n" +
                    "This program is distributed in the hope that it will be useful,\n" +
                    "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                    "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                    "GNU General Public License for more details.\n\n" +
                    "You should have received a copy of the GNU General Public License\n" +
                    "along with this program; if not, write to the Free Software Foundation,\n" +
                    "Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA\n");
            return;
        }

        CLIFileConverter converter = getConverter();

        if (converter != null) {
            converter.setConvertToTemp(getStartupFlag(CLIHelpMSGViewer.CLI_CONVERT_TEMP));
            converter.setOpenAfterConvert(getStartupFlag(CLIHelpMSGViewer.CLI_CONVERT_OPEN));
            converter.work();
        } else {
            invokeGui();
        }
    }

    private CLIFileConverter getConverter() {
        if (getStartupFlag(Msg2MBox.CLI_PARAMETER)) {
            return new Msg2MBox(this);
        } else if (getStartupFlag(MBox2Msg.CLI_PARAMETER)) {
            return new MBox2Msg(this);
        } else if (getStartupFlag(Msg2Eml.CLI_PARAMETER)) {
            return new Msg2Eml(this);
        } else if (getStartupFlag(Oft2Eml.CLI_PARAMETER)) {
            return new Oft2Eml(this);
        } else if (getStartupFlag(Eml2Msg.CLI_PARAMETER)) {
            return new Eml2Msg(this);
        } else {
            return null;
        }
    }

    @Override
    public String getVersion() {
        return Version.getVersion();
    }

    public void invokeGui() {
        if (splashEnabled()) {
            splash = new StartupWindow(
                    "/at/redeye/FrameWork/base/resources/pictures/redeye.png");
        }

        AppConfigDefinitions.registerDefinitions();
        FrameWorkConfigDefinitions.registerDefinitions();

        registerPlugins();

        setLookAndFeel();

        configureLogging();

        BaseWin mainwin = getStartupFlag(CLIHelpMSGViewer.CLI_MAINWIN)
                ? new MainWin(root)
                : new SingleWin(root);

        if (getStartupFlag(CLIHelpMSGViewer.CLI_HIDEMENUBAR)) {
            mainwin.hideMenuBar();
        }

        Arrays.stream(args)
                .filter(ModuleLauncher::isMessagePath)
                .map(File::new)
                .forEach(mainwin::openFile);

        closeSplash();
        mainwin.setVisible(true);
    }

    private void registerPlugins() {
        if (Setup.is_win_system()) {
            root.registerPlugin(new at.redeye.Plugins.ShellExec.Plugin());
        }

        root.registerPlugin(new net.sourceforge.MSGViewer.Plugins.msgparser.Plugin());
        root.registerPlugin(new net.sourceforge.MSGViewer.Plugins.tnef.Plugin());
        root.registerPlugin(new net.sourceforge.MSGViewer.Plugins.poi.Plugin());
        root.registerPlugin(new net.sourceforge.MSGViewer.Plugins.javamail.Plugin());
        root.registerPlugin(new at.redeye.Plugins.CommonsLang.Plugin());
        root.registerPlugin(new at.redeye.Plugins.JerichoHtml.Plugin());
    }

    private boolean getStartupFlag(String string) {
        return Arrays.stream(args).anyMatch(string::equalsIgnoreCase);
    }

    private static boolean isMessagePath(String arg) {
        return arg.toLowerCase().endsWith(".msg") ||
                arg.toLowerCase().endsWith(".mbox") ||
                arg.toLowerCase().endsWith(".eml");
    }
}
