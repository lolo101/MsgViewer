package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.ConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmDefaultCheckSuite;

import javax.swing.*;
import java.util.Arrays;

public class FrameWorkConfigDefinitions {

    public static final DBConfig HelpParamColorEven = new DBConfig("HelpParamColorEven", "#fefeaa", "Hilfehintergundfarbe für Parameter gerade Zeilen.");
    public static final DBConfig HelpParamColorOdd = new DBConfig("HelpParamColorOdd", "#ddeedd", "Hilfehintergundfarbe für Parameter ungerade Zeilen.");
    public static final DBConfig HelpParamColorTitle = new DBConfig("HelpParamColorTitle", "#aaddff", "Hilfehintergundfarbe für Parameter Titel.");
    public static final DBConfig SpreadSheetColorEven = new DBConfig("SpreadSheetColorEven", "#d2ebf5", "Hintergundfarbe der Tabelle bei geraden Reihen.");
    public static final DBConfig SpreadSheetColorEvenEditable = new DBConfig("SpreadSheetColorEvenEditable", "#f5f5ff", "Hintergundfarbe der Tabelle bei geraden editiebaren Reihen.");
    public static final DBConfig SpreadSheetColorOdd = new DBConfig("SpreadSheetColorOdd", "#ffffff", "Hintergundfarbe der Tabelle bei ungeraden Reihen.");
    public static final DBConfig SpreadSheetColorOddEditable = new DBConfig("SpreadSheetColorOddEditable", "#dcf5eb", "Hintergundfarbe der Tabelle bei ungeraden editiebaren Reihen.");
    public static final DBConfig SpreadSheetMarginEditable = new DBConfig("SpreadSheetMarginEditable", "20", "Zusätzlicher Rand, um den die Tabellenspalten breiter gemacht werden um besseres Editieren zu ermöglichen. Dies gilt nur für editierbare Spalten.");
    public static final DBConfig SpreadSheetMarginReadOnly = new DBConfig("SpreadSheetMarginReadOnly", "5", "Zusätzlicher Rand, um den die Tabellenspalten breiter gemacht werden. Dieser Wert gilt nur für nicht editierbare Spalten.");
    public static final DBConfig DefaultAutoLineBreakWidth = new DBConfig("DefaultAutoLineBreakWidth", "40", "Breite eines automatisch umgebrochenen Textes.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));
    private static final String[] POSSIBLE_LOOK_AND_FEEL = Arrays.stream(UIManager.getInstalledLookAndFeels()).map(UIManager.LookAndFeelInfo::getName).toArray(String[]::new);
    public static final DBConfig LookAndFeel = new DBConfig("LookAndFeel", "Nimbus",
            "Bestimmt das Aussehen der Benutzoberfläche. Mögliche Werte sind " + Arrays.toString(POSSIBLE_LOOK_AND_FEEL),
            new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LOOKANDFEEL | PrmDefaultChecksInterface.PRM_HAS_VALUE),
            POSSIBLE_LOOK_AND_FEEL);
    public static final DBConfig SpreadSheetRowHeaderLimit = new DBConfig("SpreadSheetRowHeaderLimit", "20", "Legt fest ab welcher Anzahl von Zeilen im Spreadsheet die Zeilennummer eingeblendet werden sollen.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));

    public static final DBConfig OpenCommand = new DBConfig("OpenCommand", "xdg-open", "Kommando für das öffnen einer Datei, oder eines Verzeichnisses");

    public static void registerDefinitions() {
        ConfigDefinitions.add_help_path("/at/redeye/FrameWork/base/resources/Help/Params/");

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
        addLocal(LookAndFeel);

        if( Setup.is_win_system() )
            OpenCommand.value.loadFromCopy("explorer");
        else if( Setup.is_mac_system() )
            OpenCommand.value.loadFromCopy("open");

        addLocal(OpenCommand);
    }

    private static void addLocal(DBConfig c) {
        ConfigDefinitions.add(c);
    }
}
