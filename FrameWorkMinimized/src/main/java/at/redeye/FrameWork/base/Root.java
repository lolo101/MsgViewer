/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;
import at.redeye.FrameWork.Plugin.Plugin;
import at.redeye.FrameWork.base.prm.impl.gui.GlobalConfig;
import at.redeye.FrameWork.utilities.calendar.Holidays;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author martin
 */
public abstract class Root {

    String app_name;
    String web_start_url;
    String app_title;

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     *
     * This is set to "en" by default.
     */
    String base_language;
    
    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     * 
     * This is set to "en" by default
     */
    String default_language;

    /**
     * path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     */
    String language_resource_path;

    /**
     * language used in messages, can differ from
     * Locale.getDefault()
     */
    String display_language;

    static Root static_root;

    public Root( String app_name )
    {
        this.app_name = app_name;
        static_root = this;
    }

    public Root( String app_name, String app_title )
    {
        this.app_name = app_name;
        this.app_title = app_title;
        static_root = this;
    }

    public abstract Setup getSetup();
    
    public abstract boolean saveSetup();
    
    public abstract void setDBConnection( DBConnection con );
    public abstract DBConnection getDBConnection();
    public abstract boolean loadDBConnectionFromSetup();
    
    public void informWindowOpened( BaseDialogBase dlg ) {}
    public void informWindowClosed( BaseDialogBase dlg ) {}
    public void closeAllWindowsExceptThisOne( BaseDialogBase dlg ) {}
    public void closeAllWindowsNoAppExit() {}

    public void appExit() {}
    
    public void setAktivUser( DBStrukt pb )
    { 
    
    }
    /*
    public int getUserPermissionLevel()
    {
        return UserManagementInterface.UM_PERMISSIONLEVEL_ADMIN;
    }*/
    
    public String getUserName()
    {
        return "";
    }
    
    public String getLogin()
    {
        return "";
    }
/*
    public DBBindtypeManager getBindtypeManager()
    {
        return null;
    }        
    
    public DBManager getDBManager()
    {
        return null;
    }
  */  
    public int getUserId()
    {
        return 0;
    }

    public String getAppName()
    {
        return app_name;
    }

    public void setWebStartUlr(String url)
    {
        web_start_url = url;
    }

    public String getWebStartUrl()
    {
        return web_start_url;
    }

    public String getAppTitle()
    {
        return app_title;
    }

    public void waitUntilNetworkIsReady()
    {

    }

    public void noProxyFor(String address)
    {
        
    }

    public void addDllExtractorToCache( DLLExtractor extractor )
    {

    }

    public void updateDllCache()
    {
    
    }

    public void registerPlugin( Plugin plugin )
    {

    }

    public List<Plugin> getRegisteredPlugins()
    {
        return null;
    }


    public Plugin getPlugin(String name)
    {
        return null;
    }

    public Holidays getHolidays() {
        return null;
    }

    public void setHolidays( Holidays  holidays )
    {

    }

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     *
     * This is set to "en" by default.
     */
    public void setBaseLanguage( String language )
    {
        base_language = language;
    }

    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     *
     * This is set to "en" by default
     */
    public void setDefaultLanguage( String language )
    {
        default_language = language;
    }

    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     *
     * This is set to "en" by default
     */
    public String getDefaultLanguage()
    {
        if( default_language == null )
            default_language = "en";

        return default_language;
    }

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     *
     * This is set to "en" by default.
     */
    public String getBaseLanguage()
    {
        if( base_language == null )
            base_language = "en";

        return base_language;
    }

    /**
     * path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     */
    public void setLanguageTranslationResourcePath( String path )
    {
        language_resource_path = path;
    }

    /**     
     * @return language that should be used for translations
     * can be Locale.getDefault() or something userdefined
     */
    public String getDisplayLanguage()
    {
        if (display_language == null)
        {
            Setup setup = getSetup();

            display_language = Locale.getDefault().toString();

            if (setup == null) {
                return display_language;
            }

            String lang = setup.getLocalConfig(BaseAppConfigDefinitions.DisplayLanguage);

            if (lang != null && lang.trim().isEmpty()) {
                return display_language;
            }

            display_language = lang;
        }

        return display_language;
    }

    /**
     * @return path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     * or null if not set
     */
    public String getLanguageTranslationResourcePath()
    {
        return language_resource_path;
    }

    public abstract String MlM( String message );

    /**
     * load a MlM file for a spacific class
     * @param obj
     * @param impl_locale the locale the class was originaly implemented
     * eg "de" for german
     */
    public abstract void loadMlM4Class( Object obj, String impl_locale );

    /**
     * load a MlM file for a spacific class
     * as implementation language the value of base_language is used
     * @param obj
     */
    public abstract void loadMlM4Class( Object obj );

    /**
     * load a MlM file for a spacific class
     * @param obj
     * @param impl_locale the locale the class was originaly implemented
     * eg "de" for german
     */
    public abstract void loadMlM4ClassName(String name, String string);

    /**
     * load a MlM file for a spacific class
     * as implementation language the value of base_language is used
     * @param obj
     */
    public abstract void loadMlM4ClassName(String name);

    /**
     * only use this in case of emergency
     * @return the last instace of the root class
     */
    public static Root getLastRoot()
    {
        return static_root;
    }
    
    /**     
     * @return the number of currently open windows
     */
    public abstract int countOpenWindows();
}
