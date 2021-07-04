package at.redeye.FrameWork.base;

import at.redeye.FrameWork.Plugin.Plugin;
import at.redeye.FrameWork.base.dll_cache.DLLCache;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;
import at.redeye.FrameWork.base.proxy.AutoProxyHandler;
import at.redeye.FrameWork.base.translation.MLHelper;
import at.redeye.FrameWork.utilities.calendar.CalendarFactory;
import at.redeye.FrameWork.utilities.calendar.Holidays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LocalRoot extends Root {

    protected LocalSetup setup;
    protected DBConnection db_connection;
    protected Vector<BaseDialogBase> dialogs = new Vector<>();
    protected boolean appExitAllowed = true;
    private static final Logger logger = LogManager.getLogger(LocalRoot.class);
    DelayedProxyLoader loader_proxy;
    AutoProxyHandler proxy_handler;
    DLLCache dll_cache;
    ArrayList<Plugin> plugins;
    Holidays holidays;
    MLHelper ml_helper;

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

        init(true);
    }

    public LocalRoot(String app_name, String title) {
        super(app_name, title);

        init(true);
    }

    public LocalRoot(String app_name, String title, boolean enable_proxy_loading) {
        super(app_name, title);

        init(enable_proxy_loading);
    }

    private void init(boolean enable_proxy_loading) {
        dll_cache = new DLLCache(this);
        setup = new LocalSetup(this, app_name);

        if (enable_proxy_loading) {
            loader_proxy = new DelayedProxyLoader(this);
            loader_proxy.start();
        }
    }

    @Override
    public Setup getSetup() {
        return setup;
    }

    @Override
    public boolean saveSetup() {

        if (ml_helper != null)
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
        Vector<BaseDialogBase> dlgs = new Vector<>(dialogs);

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

    @Override
    public void addDllExtractorToCache(DLLExtractor extractor) {
        dll_cache.addDllExtractor(extractor);
        dll_cache.initEnv();
    }

    @Override
    public void registerPlugin(Plugin plugin) {
        if (plugins == null)
            plugins = new ArrayList<>();
        else {
            for (Plugin p : plugins) {
                // already registered
                if (p.getName().equals(plugin.getName()))
                    return;
            }
        }

        if (plugin.isAvailable()) {
            try {
                plugin.initPlugin(this);
                plugins.add(plugin);
            } catch (AccessControlException ex) {
                logger.error(ex, ex);
            }
        }
    }

    @Override
    public List<Plugin> getRegisteredPlugins() {
        return plugins;
    }

    @Override
    public Plugin getPlugin(String name) {
        for (Plugin plugin : plugins) {
            if (plugin.getName().equals(name)) {
                if (!plugin.isAvailable())
                    return null;

                return plugin;
            }
        }

        return null;
    }

    @Override
    public Holidays getHolidays() {
        if (holidays == null)
            holidays = CalendarFactory.getDefaultHolidays();

        return holidays;
    }

    @Override
    public void setHolidays(Holidays holidays) {
        this.holidays = holidays;
    }

    @Override
    public String MlM(String message) {
        if (ml_helper == null)
            ml_helper = new MLHelper(this);

        return ml_helper.MlM(message);
    }

    /**
     * load a MlM file for a spacific class
     *
     * @param impl_locale the locale the class was originaly implemented
     *                    eg "de" for german
     */
    @Override
    public void loadMlM4Class(Object obj, String impl_locale) {
        if (ml_helper == null)
            ml_helper = new MLHelper(this);


        ml_helper.autoLoadFile4Class(obj, getDisplayLanguage(), impl_locale);
    }

    /**
     * load a MlM file for a spacific class
     *
     * @param impl_locale the locale the class was originaly implemented
     *                    eg "de" for german
     */
    @Override
    public void loadMlM4ClassName(String name, String impl_locale) {
        if (ml_helper == null)
            ml_helper = new MLHelper(this);


        ml_helper.autoLoadFile4ClassName(name, getDisplayLanguage(), impl_locale);
    }

    @Override
    public void setLanguageTranslationResourcePath(String path) {
        super.setLanguageTranslationResourcePath(path);

        if (ml_helper == null)
            ml_helper = new MLHelper(this);

        ml_helper.autoLoadCurrentLocale();
    }

}
