package at.redeye.FrameWork.base;

import at.redeye.FrameWork.utilities.ParseJNLP;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.FrameWork.widgets.StartupWindow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

public abstract class BaseModuleLauncher {
    public StartupWindow splash = null;
    public static final Logger logger = LogManager.getRootLogger();
    public Root root;
    public String[] args;
    Properties jnlp_properties;

    public BaseModuleLauncher(String[] args) {

        this.args = args;
        BaseConfigureLogging();
    }

    public String getStartupParam(String name) {
        return getStartupParam(name, name, name, null);
    }

    public String getStartupParam(String shortname, String longname, String envname) {
        return getStartupParam(shortname, longname, envname, null);
    }

    public String getStartupParam(String shortname, String longname, String envname, String default_value) {

        shortname = "-" + shortname;
        longname = "-" + longname;

        if (args != null) {
            boolean next = false;

            for (String arg : args) {
                if (next) {
                    return arg;
                } else if (arg.equalsIgnoreCase(shortname)
                        || arg.equalsIgnoreCase(longname)) {
                    next = true;
                }
            }
        }

        parseJNLP();

        String url = System.getProperty(envname.toUpperCase());

        if (url == null && jnlp_properties != null)
            url = jnlp_properties.getProperty(envname.toUpperCase());

        if (url == null || url.trim().isEmpty()) {
            String sdev = default_value;
            if (sdev == null)
                sdev = "(null)";

            if (logger.isTraceEnabled())
                logger.trace(envname + "=" + sdev + " (default)");
            return default_value;
        }

        System.out.println(envname + "=" + url);
        return url;
    }

    private void parseJNLP() {
        if (jnlp_properties != null)
            return;

        for (String arg : args) {
            if (arg.endsWith(".jnlp")) {
                final File jnlp_file = new File(arg);

                if (jnlp_file.exists()) {
                    new AutoLogger<>(BaseModuleLauncher.class.getName(),
                            () -> new ParseJNLP(jnlp_file).getProperties()
                    ).onSuccess(result -> jnlp_properties = result);
                }
            }
        }
    }

    /**
     * configures logger for the first usage
     * The default logging level is {@link Level#ALL}
     */
    public static void BaseConfigureLogging() {
        BaseConfigureLogging(Level.ALL);
    }

    /**
     * configures logger for the first usage
     *
     * @param level Logging Level
     */
    public static void BaseConfigureLogging(Level level) {

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern("%d{ISO8601} %-5p (%F:%L): %m%n")
                .build();

        ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);

        LoggerConfig loggerConfig = LoggerContext.getContext().getConfiguration().getRootLogger();
        loggerConfig.setLevel(level);
        loggerConfig.addAppender(consoleAppender, null, null);
    }

    public final void configureLogging() {

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern("%d{ISO8601} %-5p (%F:%L): %m%n")
                .build();

        ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);

        String logFileDir = root.getSetup().getLocalConfig(
                BaseAppConfigDefinitions.LoggingDir);
        logger.trace("logFileDir: " + logFileDir);
        String logFileLevel = root.getSetup().getLocalConfig(
                BaseAppConfigDefinitions.LoggingLevel);
        String loggingEnabled = root.getSetup().getLocalConfig(
                BaseAppConfigDefinitions.DoLogging);

        if (logFileDir.equals("APPHOME"))
            logFileDir = Setup.getAppConfigDir(root.getAppName() + "/log");

        String filename = logFileDir + (logFileDir.isEmpty() ? "" : "/")
                + "log.OS-" + System.getProperty("user.name", "unknown-user")
                + ".txt";

        logger.trace("Filename: " + filename);

        LoggerConfig loggerConfig = LoggerContext.getContext().getConfiguration().getRootLogger();
        loggerConfig.setLevel(Level.getLevel(logFileLevel));
        loggerConfig.addAppender(consoleAppender, null, null);

        if (loggingEnabled.equalsIgnoreCase("ja")
                || loggingEnabled.equalsIgnoreCase("yes")
                || loggingEnabled.equalsIgnoreCase("true")) {

            RollingFileAppender fileAppender = RollingFileAppender.newBuilder()
                    .setLayout(layout)
                    .withFileName(filename)
                    .setName(RollingFileAppender.class.getSimpleName())
                    .withAppend(true)
                    .build();

            loggerConfig.addAppender(fileAppender, null, null);
        }

    }

    public abstract String getVersion();

    public boolean splashEnabled() {
        return !StringUtils.isYes(getStartupParam(null, "nosplash", "NOSPLASH"));
    }

    public void closeSplash() {
        if (splash != null)
            splash.close();
    }

    /**
     * This method sets the LookAndFeel which the user has parameterized. It may
     * be called after the PrmInit was done, but it has to be done before the UI
     * starts.
     */
    public void setLookAndFeel() {

        String config = root.getSetup().getLocalConfig(
                FrameWorkConfigDefinitions.LookAndFeel);

        logger.debug("Found LookAndFeel PRM value: <" + config + ">");

        try {
            UIManager.setLookAndFeel(getLookAndFeelStrByName(config));
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
            logger.error(e.getMessage());
        }
    }

    public static String getLookAndFeelStrByName(String name) {

        if (name.equalsIgnoreCase("motif")) {
            return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        } else if (name.equalsIgnoreCase("metal")) {
            return "javax.swing.plaf.metal.MetalLookAndFeel";
        } else if (name.equalsIgnoreCase("nimbus")) {
            return "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
        } else {
            return UIManager.getSystemLookAndFeelClassName();
        }
    }
}
