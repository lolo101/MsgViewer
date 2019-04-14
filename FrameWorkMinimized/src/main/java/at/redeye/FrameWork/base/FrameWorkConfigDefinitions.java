/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.GlobalConfigDefinitions;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.PrmDefaultCheckSuite;

/**
 *
 * @author martin
 */
public class FrameWorkConfigDefinitions {

    public static DBConfig HelpParamColorEven = new DBConfig("HelpParamColorEven", "#fefeaa", "Hilfehintergundfarbe für Parameter gerade Zeilen.");
    public static DBConfig HelpParamColorOdd = new DBConfig("HelpParamColorOdd", "#ddeedd", "Hilfehintergundfarbe für Parameter ungerade Zeilen.");
    public static DBConfig HelpParamColorTitle = new DBConfig("HelpParamColorTitle", "#aaddff", "Hilfehintergundfarbe für Parameter Titel.");
    public static DBConfig SpreadSheetColorEven = new DBConfig("SpreadSheetColorEven", "#d2ebf5", "Hintergundfarbe der Tabelle bei geraden Reihen.");
    public static DBConfig SpreadSheetColorEvenEditable = new DBConfig("SpreadSheetColorEvenEditable", "#f5f5ff", "Hintergundfarbe der Tabelle bei geraden editiebaren Reihen.");
    public static DBConfig SpreadSheetColorOdd = new DBConfig("SpreadSheetColorOdd", "#ffffff", "Hintergundfarbe der Tabelle bei ungeraden Reihen.");
    public static DBConfig SpreadSheetColorOddEditable = new DBConfig("SpreadSheetColorOddEditable", "#dcf5eb", "Hintergundfarbe der Tabelle bei ungeraden editiebaren Reihen.");
    public static DBConfig SpreadSheetMarginEditable = new DBConfig("SpreadSheetMarginEditable", "20", "Zusätzlicher Rand, um den die Tabellenspalten breiter gemacht werden um besseres Editieren zu ermöglichen. Dies gilt nur für editierbare Spalten.");
    public static DBConfig SpreadSheetMarginReadOnly = new DBConfig("SpreadSheetMarginReadOnly", "5", "Zusätzlicher Rand, um den die Tabellenspalten breiter gemacht werden. Dieser Wert gilt nur für nicht editierbare Spalten.");
    public static DBConfig DefaultAutoLineBreakWidth = new DBConfig("DefaultAutoLineBreakWidth", "40", "Breite eines automatisch umgebrochenen Textes.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));
    public static DBConfig ImagePreviewInFileOpen = new DBConfig("ImagePreviewinFileOpen", "false", "Soll im Datei öffnen Dialogen die Bildervorschau angezeigt werden?", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_TRUE_FALSE));
    public static DBConfig AllowAutoLogin = new DBConfig("AllowAutoLogin", "false", "Ist eine automatische Anmeldung generell zulässig?", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_TRUE_FALSE));
    public static DBConfig AutoLoginUser = new DBConfig("AutoLoginUser", "", "Legt den Login fest, mit dem die automatische Anmeldung durchgeführt wird.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_HAS_VALUE));
    public static DBConfig LookAndFeel = new DBConfig ("LookAndFeel", "System", "Bestimmt das Aussehen der Benutzoberfläche. Mögliche Werte sind \"System\", \"Motif\", \"Nimbus\" oder \"Metal\"", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LOOKANDFEEL));
    public static DBConfig SpreadSheetRowHeaderLimit = new DBConfig("SpreadSheetRowHeaderLimit", "20", "Legt fest ab welcher Anzahl von Zeilen im Spreadsheet die Zeilennummer eingeblendet werden sollen.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));

    public static DBConfig ProxyAutoDetect = new DBConfig( "ProxyAutoDetect", "true", "Proxy Einstellungen automatisch finden", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_TRUE_FALSE));
    public static DBConfig ProxyHost = new DBConfig( "ProxyHost", "" );
    public static DBConfig ProxyPort = new DBConfig( "ProxyPort", "8080", "", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG ));
    public static DBConfig ProxyDisabledFor = new DBConfig( "ProxyDisbledFor", "", "Hostnamen, oder IP Addressen, für die der Proxy nicht verwendet werden soll");
    public static DBConfig ProxyEnabled = new DBConfig( "ProxyEnabled", "true", "", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_TRUE_FALSE));

    public static DBConfig OpenCommand = new DBConfig("OpenCommand", "xdg-open", "Kommando für das öffnen einer Datei, oder eines Verzeichnisses");

    public static void registerDefinitions() {
        GlobalConfigDefinitions.add_help_path("/at/redeye/FrameWork/base/resources/Help/Params/");
        LocalConfigDefinitions.add_help_path("/at/redeye/FrameWork/base/resources/Help/Params/");

        addLocal(HelpParamColorEven);
        addLocal(HelpParamColorOdd);
        addLocal(HelpParamColorTitle);

        addLocal(SpreadSheetColorEven);
        addLocal(SpreadSheetColorEvenEditable);
        addLocal(SpreadSheetColorOdd);
        addLocal(SpreadSheetColorOddEditable);
        addLocal(SpreadSheetMarginEditable);
        addLocal(SpreadSheetMarginReadOnly);
        addLocal(SpreadSheetRowHeaderLimit);

        addLocal(DefaultAutoLineBreakWidth);
        addLocal(ImagePreviewInFileOpen);
        addLocal(AutoLoginUser);
        addLocal(LookAndFeel);

        addLocal(ProxyAutoDetect);
        addLocal(ProxyHost);
        addLocal(ProxyPort);
        addLocal(ProxyDisabledFor);
        addLocal(ProxyEnabled);

        if( Setup.is_win_system() )
            OpenCommand.value.loadFromCopy((String)"explorer");
        else if( Setup.is_mac_system() )
            OpenCommand.value.loadFromCopy((String)"open");

        addLocal(OpenCommand);


        add(AllowAutoLogin);

    }

    static void add(String name, String value, String descr) {
        GlobalConfigDefinitions.add(new DBConfig(name, value, descr));
    }

    static void add(DBConfig c) {
        GlobalConfigDefinitions.add(c);
    }

    static void addLocal(DBConfig c) {
        LocalConfigDefinitions.add(c);
    }
}
