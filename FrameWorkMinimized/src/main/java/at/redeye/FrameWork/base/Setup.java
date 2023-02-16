package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class Setup {

    private static final Logger logger = LogManager.getLogger(Setup.class);
    private static final Pattern WINDOWS_OS_NAME = Pattern.compile(".*win.*", Pattern.CASE_INSENSITIVE);
    public static final String WindowX = "WindowX";
    public static final String WindowY = "WindowY";
    public static final String WindowWidth = "WindowWidth";
    public static final String WindowHeight = "WindowHeight";

    private static final boolean b_is_win_system = WINDOWS_OS_NAME.matcher(System.getProperty("os.name")).matches();
    private static final boolean b_is_linux_system = System.getProperty("os.name").equals("Linux");
    private static final boolean b_is_mac = System.getProperty("os.name").toLowerCase().contains("mac");
    private String config_file;
    private Properties props;

    protected Setup(String app_name) {
        String config_name = app_name + ".properties";

        config_file = getHiddenUserHomeFileName(config_name);

        String new_location = getAppConfigFile(app_name, config_name);

        File file = new File(new_location);

        if (file.exists()) {
            config_file = new_location;
            check();
        } else {
            check();
            config_file = new_location;
            saveProps();
        }
    }

    public static boolean is_win_system() {
        return b_is_win_system;
    }

    public static boolean is_linux_system() {
        return b_is_linux_system;
    }

    public static boolean is_mac_system() {
        return b_is_mac;
    }

    public final void saveProps() {
        try {
            Properties oldProps = new Properties();

            try (FileInputStream in = new FileInputStream(config_file)) {
                oldProps.load(in);
            } catch (FileNotFoundException ex) {
                logger.error(ex.toString());
            }

            Set<String> keys = oldProps.stringPropertyNames();

            for (String currKey : keys) {
                DBConfig c = LocalConfigDefinitions.get(currKey);
                if (c != null) {
                    PrmActionEvent event = new PrmActionEvent();
                    event.setOldPrmValue(oldProps.getProperty(currKey, ""));
                    event.setNewPrmValue(props.getProperty(currKey, ""));
                    event.setParameterName(currKey);
                    event.setPossibleVals(c.getPossibleValues());
                    c.updateListeners(event);
                }
            }

            try (FileOutputStream out = new FileOutputStream(config_file)) {
                props.store(out, "nix");
            }

        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
    }

    public static String getAppConfigDir( String app_name )
    {
        String name = getHiddenUserHomeFileName( app_name );

        File file = new File(name);

        if( !file.exists() )
        {
            if (!file.mkdirs()) {
                logger.error("failed createing directory" + name + " !!!");
                name = null;
            }
        }
        return name;
    }

    public String getConfig(String key, String default_value) {
        check();
        return props.getProperty(key, default_value);
    }

    public void setLocalConfig(String key, String value) {
        check();
        props.getProperty(key);
        props.setProperty(key, value);
    }

    private static String getHiddenUserHomeFileName(String name) {
        String config_path = System.getProperty("user.home");

        if (!is_win_system()) {
            name = "." + name;
        }

        return config_path + File.separator + name;
    }

    private static String getAppConfigFile(String app_name, String file_name) {
        String dir = getAppConfigDir(app_name);

        return dir + File.separator + file_name;
    }

    private void check() {
        if (props == null) {
            loadProps();
        }
    }

    private void loadProps() {
        props = new Properties();

        try (FileInputStream in = new FileInputStream(config_file)) {
            props.load(in);

        } catch (FileNotFoundException ignored) {


        } catch (IOException ioe) {

            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
    }
}
