/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;


/**
 *
 * @author martin
 */
public abstract class BaseModuleLauncher {
	public StartupWindow splash = null;
	public static Logger logger = LogManager.getRootLogger();
	public Root root;
	public String[] args;
        // public AutoImportDB auto_import_db;
        Properties jnlp_properties;

	public BaseModuleLauncher() {

                BaseConfigureLogging();
	}

	public BaseModuleLauncher(String[] args) {

                this.args = args;
                BaseConfigureLogging();
	}

	public String getWebStartUrl() {
		return getWebStartUrl(null);
	}

	public String getWebStartUrl(String default_url) {
		String value = getStartupParam("wsu", "webstarturl", "WEBSTARTURL",
				default_url);

		if (value != null && !value.trim().isEmpty()) {

			try {
				new URL(value);

                                logger.trace("webstarturl: " + value + " is a valid url");
				// System.out.println("webstarturl: " + value + " is a valid url");

				return value;
			} catch (MalformedURLException ex) {
				System.err.println("invalid url specified: " + value);
				ex.printStackTrace();
			}
		}

		return null;
	}

	public String getStartupParam(String shortname, String longname,
			String envname) {
		return getStartupParam(shortname, longname, envname, null);
	}

        public String getStartupParam(String name) {
            return getStartupParam(name, name, name);
        }

	public String getStartupParam(String shortname, String longname,
			String envname, String default_value) {

                shortname = "-" + shortname;
                longname = "-" + longname;

		if (args != null) {
			boolean next = false;

			for (String arg : args) {
				if (next) {
					return arg;
				} else if (arg.equalsIgnoreCase( shortname)
				           || arg.equalsIgnoreCase(longname)) {
					next = true;
				}
			}
		}

                parseJNLP();

		String url = System.getProperty(envname.toUpperCase());

                if( url == null && jnlp_properties != null )
                    url = jnlp_properties.getProperty(envname.toUpperCase());

		if (url == null || url.trim().isEmpty()) {
                        String sdev = default_value;
                        if( sdev == null )
                            sdev = "(null)";

                        if( logger.isTraceEnabled() )
                            logger.trace(envname + "=" +  sdev + " (default)" );
			return default_value;
		}

		System.out.println( envname + "=" + url);
		return url;
	}

    private void parseJNLP()
    {
        if( jnlp_properties != null )
            return;

        for( String arg : args )
        {
            if( arg.endsWith(".jnlp") )
            {
                final File jnlp_file = new File( arg );

                if( jnlp_file.exists() )
                {
                    new AutoLogger(BaseModuleLauncher.class.getName()) {

                        @Override
                        public void do_stuff() throws Exception {
                            ParseJNLP parser = new ParseJNLP(jnlp_file);

                            jnlp_properties = parser.getProperties();
                        }
                    };
                }
            }
        }
    }

    /**
     * will be called when jnlp update process is completet
     * overload if required
     */
    public void jnlpUpdated()
    {

    }

    /**
     * configures logger for the first usage
     * The default logging level is Leve.ALL
     */
    public static void BaseConfigureLogging()
    {
        BaseConfigureLogging(Level.ALL);
    }

    /**
     * configures logger for the first usage
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

	public void configureLogging() {

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

                if( logFileDir.equals("APPHOME") )
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

	public void setSetupParam(String value, DBConfig config,
			boolean if_not_exist) {
		if (value == null)
			return;

		if (value.trim().isEmpty())
			return;

		root.getSetup().setLocalConfig(config.getConfigName(), value,
				if_not_exist);
	}

	public void setCommonLoggingLevel() {

		String do_logging = getStartupParam("dl", "do-logging", "LOGGING");
		String level = getStartupParam("ll", "logging-level", "LOGGING_LEVEL");
		String dir = getStartupParam("ld", "logging-dir", "LOGGING_DIR");
		String force_logging = getStartupParam("fl", "force-logging",
				"FORCE_LOGGING");

		String enable_logging_on_new_version = getStartupParam("",
				"enable-logging-on-new_version",
				"ENABLE_LOGGING_ON_NEW_VERSION");

		if (StringUtils.isYes(enable_logging_on_new_version)) {
			String version = root.getSetup().getLocalConfig(
					BaseAppConfigDefinitions.Version);

			if (version == null || !version.equalsIgnoreCase(getVersion())) {
				if (!StringUtils.isYes(do_logging))
					do_logging = "true";

				if (!StringUtils.isYes(force_logging))
					force_logging = "true";
			}
		}

		root.getSetup().setLocalConfig(
				BaseAppConfigDefinitions.Version.getConfigName(), getVersion());

		if (dir != null && dir.equalsIgnoreCase("APPHOME")) {
			dir = Setup.getAppConfigDir(root.getAppName() + "/log");
		}

		boolean force = false;

		if (StringUtils.isYes(force_logging)) {
			force = true;
		}

		setSetupParam(do_logging, BaseAppConfigDefinitions.DoLogging, force);
		setSetupParam(level, BaseAppConfigDefinitions.LoggingLevel, force);
		setSetupParam(dir, BaseAppConfigDefinitions.LoggingDir, force);

		root.getSetup().saveConfig();

		// I think this is too much...

		configureLogging();
	}

	public boolean splashEnabled() {
		return !StringUtils.isYes(getStartupParam(null, "nosplash", "NOSPLASH"));
	}

        public void closeSplash()
        {
            if( splash != null )
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

    /**
     * initialises the default params for the database
     * vars, if they are not already existing
     */
    public void initDBConnectionFromParams() {
        boolean always_overwrite = StringUtils.isYes(getStartupParam(null, "dboverwrite",
                Setup.USE_DB_CONNECTION_ALWAYS_FROM_JNLP));

        initIfSet(Setup.DBDatabase, always_overwrite);
        initIfSet(Setup.DBHost, always_overwrite);
        initIfSet(Setup.DBInstance, always_overwrite);
        initIfSet(Setup.DBPasswd, always_overwrite);
        initIfSet(Setup.DBPort, always_overwrite);
        initIfSet(Setup.DBType, always_overwrite);
        initIfSet(Setup.DBUser, always_overwrite);
        initIfSet(Setup.EncryptAllDBSettings,always_overwrite);

        root.saveSetup();
    }

    /**
     * Sets a local config param if it is set detected by <b>getStartupParam()</b>
     * @param param Name of the parameter
     * @param always_over_write set it to true, if you wan't to override existing settings.
     */
        public void initIfSet( String param, boolean always_over_write )
        {
            String val = getStartupParam(param, param, param);

            if( val != null )
            {
                root.getSetup().setLocalConfig(param, val, !always_over_write);
            }
        }
}
