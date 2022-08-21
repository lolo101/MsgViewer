package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.regex.Pattern;

public abstract class Setup {

    private static final Pattern WINDOWS_OS_NAME = Pattern.compile(".*win.*", Pattern.CASE_INSENSITIVE);
    static public final String WindowX = "WindowX";
    static public final String WindowY = "WindowY";
    static public final String WindowWidth = "WindowWidth";
    static public final String WindowHeight = "WindowHeight";

    public static Logger logger = LogManager.getLogger(Setup.class);

    private static final boolean b_is_win_system = WINDOWS_OS_NAME.matcher(System.getProperty("os.name")).matches();
    private static final boolean b_is_linux_system = System.getProperty("os.name").equals("Linux");
    private static final boolean b_is_mac = System.getProperty("os.name").toLowerCase().contains("mac");

    public static boolean is_win_system()
    {
        return b_is_win_system;
    }

    public static boolean is_linux_system()
    {
        return b_is_linux_system;
    }

    public static boolean is_mac_system()
    {
        return b_is_mac;
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
}
