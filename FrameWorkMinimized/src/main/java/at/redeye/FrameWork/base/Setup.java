/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;



/**
 *
 * @author martin
 */
public abstract class Setup {

    static public final String USE_DB_CONNECTION_ALWAYS_FROM_JNLP="USE_DB_CONNECTION_ALWAYS_FROM_JNLP";
    static public final String DBType = "DBType";
    static public final String DBHost = "DBHost";
    static public final String DBInstance = "DBInstance";
    static public final String DBPort = "DBPort";
    static public final String DBUser = "DBUser";
    static public final String DBPasswd = "DBPasswd";
    static public final String DBDatabase = "DBDatabase";
    static public final String EncryptAllDBSettings = "ENCRYPT_ALL_DB_SETTINGS";
    static public final String H1IPAddress = "H1IPAddress";
    static public final String H1Port = "H1Port";
    static public final String H1LTSAP = "H1LTSAP";
    static public final String H1RTSAP = "H1RTSAP";

    static public final String WindowX = "WindowX";
    static public final String WindowY = "WindowY";
    static public final String WindowWidth = "WindowWidth";
    static public final String WindowHeight = "WindowHeight";

    public static Logger logger = LogManager.getLogger(Setup.class);

    private static final boolean b_is_win_system = System.getProperty("os.name").matches(".*[Ww][Ii][Nn].*");
    private static final boolean b_is_win_7_system  = System.getProperty("os.name").matches("Windows 7");
    private static final boolean b_is_linux_system = System.getProperty("os.name").equals("Linux");
    private static final boolean b_is_65bit_system = System.getProperty("os.arch").contains("64");
    private static final boolean b_is_mac = System.getProperty("os.name").toLowerCase().contains("mac");
    private static final boolean b_is_sun = System.getProperty("os.name").toLowerCase().contains("sunos");

    public static boolean is_win_system()
    {
        return b_is_win_system;
    }

    public static boolean is_win_7_system()
    {
        return b_is_win_7_system;
    }

    public static boolean is_linux_system()
    {
        return b_is_linux_system;
    }

    public static boolean is_64bit_system()
    {
        return b_is_65bit_system;
    }

    public static boolean is_mac_system()
    {
        return b_is_mac;
    }

    public static boolean is_sun_system()
    {
        return b_is_sun;
    }

    public static String getHiddenUserHomeFileName( String name )
    {
        String config_path = System.getProperty("user.home");

        if( !is_win_system() )
        {
           name = "." + name;
        }

        return config_path + File.separator + name;
    }

    public static String getAppConfigDir( String app_name )
    {
        String name = getHiddenUserHomeFileName( app_name );

        File file = new File(name);

        if( !file.exists() )
        {
            if( !file.mkdirs() )
            {
                logger.error("failed createing directory" + name + " !!!");
                name = null;
            }
        }
        return name;
    }

    public static String getAppConfigFile( String app_name, String file_name )
    {
        String dir = getAppConfigDir(app_name);

        return dir + File.separator + file_name;
    }

    public String getConfig(DBConfig config) {
        return getConfig( config.getConfigName(), config.getConfigValue() );
    }

    public String getLocalConfig(DBConfig config) {
        return getLocalConfig( config.getConfigName(), config.getConfigValue());
    }

    public abstract String getLocalConfig( String key, String default_value );

    public abstract String getConfig( String key, String default_value );

    public abstract DBConfig getConfig( String key);

    public abstract DBConfig getLocalConfig(String key);

    /**
     * set a local parameter in the config file
     * @param key   key element
     * @param value your data
     * @param if_not_exists set it to true and the parameter won't be overwritten,
     * if it already exists.
     */
    public abstract void setLocalConfig(String key, String value, boolean if_not_exists);

    /**
     * set a local parameter in the config file
     * @param key   key element
     * @param value your data
     */
    public abstract void setLocalConfig( String key, String value );

    public abstract void setConfig( String key, String value, boolean if_not_exists );

    public abstract void setConfig( String key, String value );

    public void saveConfig() {}

    /**
     * @return true if this is the first start of this appliaction for this computer.
     * simple checks, if the config file, of the appliaction already existed before, or not.
     */
    public boolean initialRun()
    {
        return false;
    }
}
