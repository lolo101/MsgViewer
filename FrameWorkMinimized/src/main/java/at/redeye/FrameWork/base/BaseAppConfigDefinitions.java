/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.GlobalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmDefaultCheckSuite;
import org.apache.logging.log4j.Level;


/**
 *
 * @author martin
 */
public class BaseAppConfigDefinitions
{
    public static DBConfig DoLogging = new DBConfig("Log-Meldungen Schreiben", "NEIN", "Sollen Logmeldungn in einer LogDatei mitgeschrieben werden.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_TRUE_FALSE));
    public static DBConfig LoggingDir = new DBConfig("Log-Verzeichnis", "APPHOME", "Verzeichnis in das die Logdateien geschrieben werden sollen.");
    private static String [] validLevels = {"MML", Level.DEBUG.toString(), Level.TRACE.toString(), Level.ALL.toString(), Level.INFO.toString()};
    public static DBConfig LoggingLevel = new DBConfig("Log-Level", "DEBUG", "Schwellwert für die Informationen in der Logdatei.", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_HAS_VALUE), validLevels);
    public static DBConfig Version = new DBConfig("Programm-Version", "0", "Programmversion mit der die Einstellungen zuletzt gespeichert wurden." );
    public static DBConfig VerticalScrollingSpeed = new DBConfig("VerticalScrollingSpeed","16","Vertikale Mausradscrollgeschwindigkeit", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));
    public static DBConfig HorizontalScrollingSpeed = new DBConfig("HorizontalScrollingSpeed","16","Horiziontale Mausradscrollgeschwindigkeit", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));
    public static DBConfig DateFormat = new DBConfig( "DateFormat", "yyyy-MM-dd", "Datumsformat");
    public static DBConfig DisplayLanguage = new DBConfig( "DisplayLanguage", "", "Sprache zb: en, oder de.");

    public static void BaseRegisterDefinitions()
    {
       addLocal(DoLogging);
       addLocal(LoggingDir);
       addLocal(LoggingLevel);
       addLocal(Version);
       addLocal(VerticalScrollingSpeed);
       addLocal(HorizontalScrollingSpeed);
       addLocal(DateFormat);
       addLocal(DisplayLanguage);
    }

    public static void add( String name, String value, String descr )
    {
        GlobalConfigDefinitions.add(new DBConfig(name,value,descr));
    }

    public static void add( DBConfig c )
    {
        GlobalConfigDefinitions.add(c);
    }


    public static void addLocal( DBConfig c )
    {
        LocalConfigDefinitions.add(c);
    }

}
