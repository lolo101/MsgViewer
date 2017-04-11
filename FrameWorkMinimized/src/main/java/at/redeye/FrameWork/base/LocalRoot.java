/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.dll_cache.DLLCache;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;
import at.redeye.FrameWork.base.proxy.AutoProxyHandler;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.FrameWork.Plugin.Plugin;
import at.redeye.FrameWork.base.translation.MLHelper;
import at.redeye.FrameWork.utilities.calendar.CalendarFactory;
import at.redeye.FrameWork.utilities.calendar.Holidays;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.ConnectionDefinition;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.MissingConnectionParamException;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.SupportedDBMSTypes;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.UnSupportedDatabaseException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author martin
 */
public class LocalRoot extends Root {

	protected LocalSetup setup;
	protected DBConnection db_connection;
	//protected DBPb userEntry = null;
//	protected DBManager dbmanager = null;
	protected Vector<BaseDialogBase> dialogs = new Vector<BaseDialogBase>();
	protected boolean appExitAllowed = true;
	private static Logger logger = Logger.getLogger(LocalRoot.class);
	// EncryptedDBPasswd enc;
	// DelayedLoader loader_encryption;
	DelayedProxyLoader loader_proxy;
	AutoProxyHandler proxy_handler;
        DLLCache dll_cache;
        ArrayList<Plugin> plugins;
        Holidays holidays;
        MLHelper ml_helper;

    @Override
    public boolean loadDBConnectionFromSetup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	public class DelayedProxyLoader extends Thread {
		Root root;

		DelayedProxyLoader(Root root) {
			this.root = root;

                        this.setName(DelayedProxyLoader.class.getCanonicalName());
		}

		@Override
		public void run() {
                    long start = System.currentTimeMillis();
                    proxy_handler = new AutoProxyHandler(root);

                    logger.trace(" proxy laoding: " + (System.currentTimeMillis() - start));
		}
	}

	public LocalRoot(String app_name) {
		super(app_name);

		init(true, true);
	}

	public LocalRoot(String app_name, String title) {
		super(app_name, title);

		init(true, true);
	}

        public LocalRoot(String app_name, String title, boolean enable_encryption, boolean enable_proxy_loading) {
		super(app_name, title);

		init(enable_encryption, enable_proxy_loading);
	}

	private void init(  boolean enable_encryption, boolean enable_proxy_loading )
        {
            dll_cache = new DLLCache(this);
/*
            if( enable_encryption ) {
                loader_encryption = new DelayedLoader();
                loader_encryption.start();
            }
*/
            setup = new LocalSetup(this, app_name);

            if( enable_proxy_loading ) {
                loader_proxy = new DelayedProxyLoader(this);
                loader_proxy.start();
            }

//            dbmanager = new DatabaseManager(this);
	}

	@Override
	public Setup getSetup() {
		return setup;
	}

	@Override
	public boolean saveSetup() {

                if( ml_helper != null )
                    ml_helper.saveMissingProps();

		if (setup.saveProps())
			return setup.saveGlobalProps();
                    
		return false;
	}

	@Override
	public void setDBConnection(DBConnection con) {
		if (db_connection != null)
			db_connection.close();

		db_connection = con;
	}

	@Override
	public DBConnection getDBConnection() {
		return db_connection;
	}

	public void closeDBConnection() {
		setDBConnection(null);
	}



	@Override
	public void informWindowOpened(BaseDialogBase dlg) {
		dialogs.add(dlg);
	}

	@Override
	public void informWindowClosed(BaseDialogBase dlg) {
		dialogs.remove(dlg);

		if (dialogs.size() <= 0) {
			if (appExitAllowed) {
				System.out.println("All Windows closed, normal exit");
				appExit();
			}
		}
	}

	@Override
	public void closeAllWindowsNoAppExit() {
		appExitAllowed = false;
		closeAllWindowsExceptThisOne(null);
		appExitAllowed = true;
	}

	@Override
	public void closeAllWindowsExceptThisOne(BaseDialogBase dlg) {
		Vector<BaseDialogBase> dlgs = new Vector<BaseDialogBase>();
		dlgs.addAll(dialogs);

		for (BaseDialogBase frame : dlgs) {
			if (frame != dlg)
				frame.closeNoAppExit();
		}
	}

	@Override
	public void appExit() {
		saveSetup();
		closeDBConnection();
		System.exit(0);
	}

        /*
	@Override
	public void setAktivUser(DBStrukt pb) {
		if (DBPb.class.isInstance(pb)) {
			userEntry = (DBPb) pb;
		}
	}*/

        /*
	@Override
	public String getLogin() {
		if (userEntry == null)
			return "";

		return userEntry.login.toString();
	}
        */
        /*
	@Override
	public String getUserName() {
		if (userEntry == null)
			return "";

		return userEntry.getUserName();
	}
        */
/*
	@Override
	public int getUserPermissionLevel() {
		if (userEntry == null)
			return UserManagementInterface.UM_PERMISSIONLEVEL_ADMIN;

		return (Integer) userEntry.plevel.getValue();
	}
        */
/*
	@Override
	public DBBindtypeManager getBindtypeManager() {
		return (DBBindtypeManager) dbmanager;
	}

	@Override
	public DBManager getDBManager() {
		return dbmanager;
	}
*/
        /*
	@Override
	public int getUserId() {
		if (userEntry == null) {
			logger.warn("userEntry is null returning default user id 0");
			return 0;
		}

		return (Integer) userEntry.id.getValue();
	}*/

	@Override
	public void noProxyFor(String address) {
		waitUntilNetworkIsReady();

		proxy_handler.exludeFromProxy(address);
	}

	@Override
	public void waitUntilNetworkIsReady() {
		if (proxy_handler == null && loader_proxy != null) {
                    try {
                        long start = System.currentTimeMillis();
                        loader_proxy.join();
                        System.out.println("                       waited for proxy "
                                + (System.currentTimeMillis() - start));

                    } catch (InterruptedException ex) {
                        logger.error(StringUtils.exceptionToString(ex));
                    }
		}

		loader_proxy = null;
	}

    @Override
    public void addDllExtractorToCache( DLLExtractor extractor )
    {
        dll_cache.addDllExtractor(extractor);
        dll_cache.initEnv();
    }

    @Override
    public void updateDllCache()
    {
        dll_cache.update();
    }

    @Override
    public void registerPlugin( Plugin plugin )
    {
        if( plugins == null )
            plugins = new ArrayList<Plugin>();
        else
        {
            for( Plugin p : plugins )
            {
                // already registered
                if( p.getName().equals(plugin.getName()) )
                    return;
            }
        }

        if( plugin.isAvailable() )
        {   
            try {
                plugin.initPlugin(this);
                plugins.add(plugin);
            } catch( AccessControlException ex ) {
                logger.error(ex,ex);                
            }            
        }
    }

    @Override
    public List<Plugin> getRegisteredPlugins()
    {
        return plugins;
    }

    @Override
    public Plugin getPlugin( String name )
    {
        for( Plugin plugin : plugins )
        {
            if( plugin.getName().equals(name) )
            {
                if( !plugin.isAvailable() )
                    return null;

                return plugin;
            }
        }

        return null;
    }

    @Override
    public Holidays getHolidays() 
    {
        if( holidays == null )
            holidays = CalendarFactory.getDefaultHolidays();

        return holidays;
    }

    @Override
    public void setHolidays( Holidays  holidays )
    {
        this.holidays = holidays;
    }

    @Override
    public String MlM( String message )
    {
        if( ml_helper == null )
            ml_helper = new MLHelper( this );

        return ml_helper.MlM(message);
    }

    /**
     * load a MlM file for a spacific class
     * @param obj
     * @param impl_locale the locale the class was originaly implemented
     * eg "de" for german
     */
    @Override
    public void loadMlM4Class( Object obj, String impl_locale )
    {
        if( ml_helper == null )
            ml_helper = new MLHelper( this );


        ml_helper.autoLoadFile4Class(obj, getDisplayLanguage(), impl_locale);
    }

    /**
     * load a MlM file for a spacific class
     * as implementation language the value of base_language is used
     * @param obj
     */
    @Override
    public void loadMlM4Class( Object obj )
    {
        if( ml_helper == null )
            ml_helper = new MLHelper( this );

        ml_helper.autoLoadFile4Class(obj, getDisplayLanguage(), base_language);
    }

    /**
     * load a MlM file for a spacific class
     * @param obj
     * @param impl_locale the locale the class was originaly implemented
     * eg "de" for german
     */
    @Override
    public void loadMlM4ClassName(String name, String impl_locale)
    {
        if( ml_helper == null )
            ml_helper = new MLHelper( this );


        ml_helper.autoLoadFile4ClassName(name, getDisplayLanguage(), impl_locale);
    }

    /**
     * load a MlM file for a spacific class
     * as implementation language the value of base_language is used
     * @param obj
     */
    @Override
    public void loadMlM4ClassName(String name)
    {
        if( ml_helper == null )
            ml_helper = new MLHelper( this );

        ml_helper.autoLoadFile4ClassName(name, getDisplayLanguage(), base_language);
    }

    @Override
    public void setLanguageTranslationResourcePath( String path )
    {
        super.setLanguageTranslationResourcePath(path);

        if( ml_helper == null )
            ml_helper = new MLHelper( this );

        ml_helper.autoLoadCurrentLocale();
    }


    /**
     * @return the number of currently open windows
     */
    @Override
    public int countOpenWindows()
    {
        return dialogs.size();
    }

    public AutoProxyHandler getAutProxyHandler()
    {
        waitUntilNetworkIsReady();
        
        return proxy_handler;
    }
}
