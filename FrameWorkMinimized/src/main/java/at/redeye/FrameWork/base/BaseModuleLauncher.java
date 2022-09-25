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
import java.util.stream.Stream;

public abstract class BaseModuleLauncher {
    protected StartupWindow splash;
    public static final Logger logger = LogManager.getRootLogger();
    public Root root;
    public final String[] args;

    public BaseModuleLauncher(String... args) {

        this.args = args;
        BaseConfigureLogging();
    }

    private String getNoSplash() {
        return Stream.of(args)
                .filter("-nosplash"::equalsIgnoreCase)
                .findFirst()
                .orElseGet(this::getNoSplashProperty);
    }

    private String getNoSplashProperty() {

        String envValue = System.getProperty("NOSPLASH");
        if (envValue != null) {
            System.out.println("env NOSPLASH=" + envValue);
            return envValue;
        }

        Properties jnlp_properties = findJnlpProperties();
        String jnlpValue = jnlp_properties.getProperty("NOSPLASH");
        if (jnlpValue != null) {
            System.out.println("JNLP NOSPLASH" + "=" + jnlpValue);
            return jnlpValue;
        }

        if (logger.isTraceEnabled())
            logger.trace("NOSPLASH undefined");
        return null;
    }

    private Properties findJnlpProperties() {
        return Stream.of(args)
                .filter(arg -> arg.endsWith(".jnlp"))
                .map(File::new)
                .filter(File::exists)
                .findFirst()
                .flatMap(jnlp_file -> new AutoLogger<>(BaseModuleLauncher.class.getName(),
                        () -> new ParseJNLP(jnlp_file).getProperties()
                ).result())
                .orElseGet(Properties::new);
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

    protected final void configureLogging() {

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

    protected final boolean splashEnabled() {
        return !StringUtils.isYes(getNoSplash());
    }

    protected final void closeSplash() {
        if (splash != null)
            splash.close();
    }

    /**
     * This method sets the LookAndFeel which the user has parameterized. It may
     * be called after the PrmInit was done, but it has to be done before the UI
     * starts.
     */
    protected final void setLookAndFeel() {

        String config = root.getSetup().getLocalConfig(
                FrameWorkConfigDefinitions.LookAndFeel);

        logger.debug("Found LookAndFeel PRM value: <" + config + ">");

        try {
            UIManager.setLookAndFeel(getLookAndFeelStrByName(config));
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
            logger.error(e.getMessage());
        }
    }

    private static String getLookAndFeelStrByName(String name) {

        if (name.equalsIgnoreCase("motif")) return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        if (name.equalsIgnoreCase("metal")) return "javax.swing.plaf.metal.MetalLookAndFeel";
        if (name.equalsIgnoreCase("nimbus")) return "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
        return UIManager.getSystemLookAndFeelClassName();
    }
}
