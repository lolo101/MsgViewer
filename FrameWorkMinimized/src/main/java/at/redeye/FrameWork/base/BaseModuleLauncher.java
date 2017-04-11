/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.transaction.Transaction;
import at.redeye.FrameWork.utilities.ParseJNLP;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.FrameWork.widgets.StartupWindow;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;


/**
 * 
 * @author martin
 */
public abstract class BaseModuleLauncher {	
	public StartupWindow splash = null;
	public static Logger logger = Logger.getRootLogger();
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
			URL arg_url;

			try {
				arg_url = new URL(value);

                                logger.trace("webstarturl: " + value + " is a valid url");
				// System.out.println("webstarturl: " + value + " is a valid url");

				return value;
			} catch (MalformedURLException ex) {
				System.err.println("invalid url specified: " + value);
				System.err.println(ex);
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

                if( url != null )    
                {
                   System.out.println( envname + "=" + url);
                }

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
/*
    public void updateJnlp() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                setName("updateJnlp");

                if (DesktopLauncher.canCreateDesktopIcon()) {

                    root.waitUntilNetworkIsReady();
                    
                    DesktopLauncher launcher = new DesktopLauncher(root);

                    if (launcher.download_jnlp()) {
                        logger.info("updated jnlp");
                    } else {
                        logger.error("failed updating jnlp");
                    }
                    
                }                

                jnlpUpdated();

                root.updateDllCache();
            }
        };

        thread.start();
    }
    */

    /**
     * creates a desktop icon using DesktpLauncher2
     * DesktopLaunscher2 does not uses Java Webstart to launch the application
     * it is downloading the jar files. Therefor on the webspace there has to be a file
     * calles md5.txt which containes all md5 sums of all jar files
     *
     * Desktoplauncher2 is downloading each file that dows not exists, or has a
     * different md5 sum thn the current jar file.
     */
    /*
    public void updateJnlp2() {

        Thread thread = new Thread() {

            @Override
            public void run() {

                setName("updateJnlp2");

                if (DesktopLauncher2.canCreateDesktopIcon()) {

                    root.waitUntilNetworkIsReady();

                    final DesktopLauncher2 launcher = new DesktopLauncher2(root);

                    if (launcher.download_jnlp_only()) {
                        logger.info("updated jnlp");

                        new AutoLogger(BaseModuleLauncher.class.getName()) {

                            @Override
                            public void do_stuff() throws Exception {
                                launcher.download_jars();
                            }
                        };

                    } else {
                        logger.error("failed updating jnlp");
                    }                    

                    jnlpUpdated();
                }

                root.updateDllCache();
            }
        };

        thread.start();
    }
*/
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
        
        logger.removeAllAppenders();
        
        PatternLayout layout = new PatternLayout(
                "%d{ISO8601} %-5p (%F:%L): %m%n");

        ConsoleAppender consoleAppender = new ConsoleAppender(layout);

        logger.setLevel(level);
        logger.addAppender(consoleAppender);
    }

	public void configureLogging() {

            logger.removeAllAppenders();

		PatternLayout layout = new PatternLayout(
				"%d{ISO8601} %-5p (%F:%L): %m%n");		

                ConsoleAppender consoleAppender = new ConsoleAppender(layout);

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

		logger.setLevel(Level.toLevel(logFileLevel));
                logger.addAppender(consoleAppender);


		if (loggingEnabled.equalsIgnoreCase("ja")
				|| loggingEnabled.equalsIgnoreCase("yes")
				|| loggingEnabled.equalsIgnoreCase("true")) {

			try {

				RollingFileAppender fileAppender = new RollingFileAppender(
						layout, filename);
				fileAppender.setAppend(true);
				fileAppender.setMaxFileSize("3MB");
				fileAppender.setName(RollingFileAppender.class.getSimpleName());

				logger.addAppender(fileAppender);

			} catch (IOException e) {
				JOptionPane
						.showMessageDialog(
								null,
								StringUtils
										.autoLineBreak("Das Logger konnte nicht korrekt initialisiert werden!"),
								"User Management", JOptionPane.WARNING_MESSAGE);

			}
		}

	}

	public void checkTableVersions() {
		new AutoLogger(BaseModuleLauncher.class.getCanonicalName()) {

			@Override
			public void do_stuff() throws Exception {

				Transaction trans = root.getDBConnection()
						.getDefaultTransaction();

				if (trans.isOpen()) {
                                    /*
					root.getBindtypeManager().setTransaction(trans);
					root.getBindtypeManager()
							.check_table_versions_with_message(
									root.getUserPermissionLevel());
                                    */
				}
			}
		};
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
		if (StringUtils.isYes(getStartupParam(null, "nosplash", "NOSPLASH"))) {
			return false;
		}

		return true;
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
	public void setLookAndFeel(Root root) {

		String config = root.getSetup().getLocalConfig(
				FrameWorkConfigDefinitions.LookAndFeel);

		logger.debug("Found LookAndFeel PRM value: <" + config + ">");

		try {
			UIManager.setLookAndFeel(getLookAndFeelStrByName(config));
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
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

        /**
         * If you wan't using autoimport a DB call this function withing in the constructor
         * This detects if autoimport is wanted, or not. This first part of the
         * Autoimport only downloads the database.
         *
         * <ul>
         *  <li><b>AUTOIMPORTDB <i>URL</i></b> must be set</li>
         *  <li><b>DBDatabase<b> eg: APPHOPME/db for derby</li>
         *  <li><b>DBType</b> eg: DB_JAVADB for derby</li>
         *  <li>.... or other Database settings</li>
         * </ul>
         *
         * This function should be called after <b>initDBConnectionFromParams()</b>
         *
         * The Function <b>autoImportDBStep2()</b> should then called withing the invoke code
         * after registering of the tables.
         *
         * @return false if something failed
         */
        /*
        public boolean autoImportDBStep1()
        {
            auto_import_db = new AutoImportDB(root, this);

            if (auto_import_db.shouldAutoImportDB() ||
                auto_import_db.shouldDownloadDB() ) {
                logger.info("Downloading DEMO Database");

                AutoMBox mb = new AutoMBox(BaseModuleLauncher.class.getName()) {

                    public void do_stuff() throws Exception {

                        root.waitUntilNetworkIsReady();

                        if (!auto_import_db.downloadDB()) {
                            JOptionPane.showMessageDialog(null, "Fehler beim Herunterladen der Demo Datenbank.");
                            logical_failure = true;
                            return;
                        }
                    }
                };

                if (mb.isFailed()) {
                    return false;
                } else {
                    logger.info("Dabase downloaded");
                }
            }

            return true;
        }
        */
    /**
     * If you wan't using autoimport a DB call this function withing in the <b>invoke()</b> function.
     * This detects if autoimport is wanted, or not. The first function
     * you hopefully calles was <b>autoImportDBStep1()</b> withing the constructor.
     * This function now really imports the database. The function is listening on following
     * Parameters:
     *
     * <ul>
     *  <li><b>AUTOIMPORTDB <i>URL</i></b> must be set</li>
     *  <li><b>AutoLoginUser <i>UserName</i></b> can be preset to automatically the autologin procedure.</li>
     *  <li><b>DBDatabase<b> eg: APPHOPME/db for derby</li>
     *  <li><b>DBType</b> eg: DB_JAVADB for derby</li>
     *  <li>.... or other Database settings</li>
     * </ul>
     *
     * This function should be called after registering the tables.
     *
     *
     * @return false if something failed
     */
        /*
    public boolean autoImportDBStep2() {

        if (auto_import_db.shouldAutoImportDB()) {
            logger.info("Importing DEMO Database");

            AutoMBox mb = new AutoMBox(BaseModuleLauncher.class.getName()) {

                @Override
                public void do_stuff() throws Exception {
                    if (!auto_import_db.importDB()) {
                        JOptionPane.showMessageDialog(null, "Fehler beim Importieren der Demo Datenbank.");
                        logical_failure = true;
                        return;
                    }
                }
            };

            if (mb.isFailed()) {
                return false;
            } else {
                logger.info("Dabase imported");
            }

            initIfSet("AutoLoginUser", false);
        }

        return true;
    }
        */
}
