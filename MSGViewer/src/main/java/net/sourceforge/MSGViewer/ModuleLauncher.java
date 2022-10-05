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

        root = new LocalRoot("MSGViewer");
        root.setStartupArgs(args);
        root.setBaseLanguage("en");
        root.setDefaultLanguage("en");
        root.setLanguageTranslationResourcePath("/net/sourceforge/MSGViewer/resources/translations");
    }

    public static void main(String[] args) {
        new ModuleLauncher(args).invoke();
    }

    private void invoke() {
        if (anyArgumentMatches(CLIHelpMSGViewer.CLI_HELP)) {
            CLIHelpMSGViewer help = new CLIHelpMSGViewer(this);
            help.printHelpScreen();
            return;
        }

        if (anyArgumentMatches(CLIHelpMSGViewer.CLI_VERSION)) {
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
            converter.setConvertToTemp(anyArgumentMatches(CLIHelpMSGViewer.CLI_CONVERT_TEMP));
            converter.setOpenAfterConvert(anyArgumentMatches(CLIHelpMSGViewer.CLI_CONVERT_OPEN));
            converter.work();
        } else {
            invokeGui();
        }
    }

    private CLIFileConverter getConverter() {
        if (anyArgumentMatches(Msg2MBox.CLI_PARAMETER)) return new Msg2MBox(this);
        if (anyArgumentMatches(MBox2Msg.CLI_PARAMETER)) return new MBox2Msg(this);
        if (anyArgumentMatches(Msg2Eml.CLI_PARAMETER)) return new Msg2Eml(this);
        if (anyArgumentMatches(Oft2Eml.CLI_PARAMETER)) return new Oft2Eml(this);
        if (anyArgumentMatches(Eml2Msg.CLI_PARAMETER)) return new Eml2Msg(this);
        return null;
    }

    @Override
    public String getVersion() {
        return Version.getVersion();
    }

    private void invokeGui() {
        if (splashEnabled()) {
            splash = new StartupWindow(
                    "/at/redeye/FrameWork/base/resources/pictures/redeye.png");
        }

        AppConfigDefinitions.registerDefinitions();
        FrameWorkConfigDefinitions.registerDefinitions();

        registerPlugins();

        setLookAndFeel();

        configureLogging();

        BaseWin mainwin = anyArgumentMatches(CLIHelpMSGViewer.CLI_MAINWIN)
                ? new MainWin(root)
                : new SingleWin(root);

        if (anyArgumentMatches(CLIHelpMSGViewer.CLI_HIDEMENUBAR)) {
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
    }

    private boolean anyArgumentMatches(String string) {
        return Arrays.stream(args).anyMatch(string::equalsIgnoreCase);
    }

    private static boolean isMessagePath(String arg) {
        return arg.toLowerCase().endsWith(".msg") ||
                arg.toLowerCase().endsWith(".mbox") ||
                arg.toLowerCase().endsWith(".eml");
    }
}
