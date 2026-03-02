package at.redeye.FrameWork.base;

import static java.nio.file.StandardOpenOption.CREATE;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.*;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.logging.log4j.*;

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
    private final Path config_file;
    private final Properties props;

    protected Setup(Path userHome, String app_name) {
        config_file = getAppConfigFile(userHome, app_name);

        if (Files.exists(config_file)) {
            props = loadProps();
        } else {
            props = new Properties();
            writeAppConfigFile(props);
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

            try (InputStream in = Files.newInputStream(config_file)) {
                oldProps.load(in);
            } catch (FileNotFoundException ex) {
                logger.error(ex.toString());
            }

            writeAppConfigFile(oldProps);

        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
    }

    private static Path getAppConfigFile(Path userHome, String app_name) {
        String file_name = app_name + ".properties";

        Path dir = getAppConfigDir(userHome, app_name);

        return dir.resolve(file_name);
    }

    public static Path getAppConfigDir(Path userHome, String app_name)
    {
        Path name = getHiddenUserHomeFileName(userHome, app_name);

        try {
            Files.createDirectories(name);
        } catch (IOException ignored) {
            logger.error("failed createing directory {} !!!", name);
        }

        return name;
    }

    private static Path getHiddenUserHomeFileName(Path userHome, String name) {

        if (!is_win_system()) {
            name = "." + name;
        }

        return userHome.resolve(name);
    }

    public String getConfig(String key, String default_value) {
        return props.getProperty(key, default_value);
    }

    public void setLocalConfig(String key, String value) {
        props.setProperty(key, value);
    }

    private Properties loadProps() {
        Properties properties = new Properties();

        try (InputStream in = Files.newInputStream(config_file)) {
            properties.load(in);
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
        return properties;
    }

    private void writeAppConfigFile(Properties oldProps) {
        for (String currKey : oldProps.stringPropertyNames()) {
            DBConfig c = ConfigDefinitions.get(currKey);
            if (c != null) {
                PrmActionEvent event = new PrmActionEvent();
                event.setOldPrmValue(oldProps.getProperty(currKey, ""));
                event.setNewPrmValue(props.getProperty(currKey, ""));
                event.setParameterName(currKey);
                event.setPossibleVals(c.getPossibleValues());
                c.updateListeners(event);
            }
        }

        try (OutputStream out = Files.newOutputStream(config_file, CREATE)) {
            props.store(out, "nix");
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
    }
}
