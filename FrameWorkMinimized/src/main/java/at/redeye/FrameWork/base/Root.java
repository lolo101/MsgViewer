package at.redeye.FrameWork.base;

import at.redeye.FrameWork.Plugin.Plugin;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;
import at.redeye.FrameWork.utilities.Storage;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public abstract class Root {

    protected final String app_name;
    private final String app_title;

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     * <p>
     * This is set to "en" by default.
     */
    private String base_language;

    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     * <p>
     * This is set to "en" by default
     */
    private String default_language;

    /**
     * path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     */
    private String language_resource_path;

    /**
     * language used in messages, can differ from
     * Locale.getDefault()
     */
    private String display_language;

    private static Root static_root;
    private final Path storage;
    private String[] startupArgs;

    public Root(String app_name, String app_title) {
        this.app_name = app_name;
        this.app_title = app_title;
        static_root = this;
        storage = Storage.getEphemeralStorage(this.app_name);
    }

    public abstract Setup getSetup();

    public abstract boolean saveSetup();

    public abstract void setDBConnection( DBConnection con );
    public abstract DBConnection getDBConnection();

    public void informWindowOpened( BaseDialogBase dlg ) {}
    public void informWindowClosed( BaseDialogBase dlg ) {}
    public void closeAllWindowsExceptThisOne( BaseDialogBase dlg ) {}
    public void closeAllWindowsNoAppExit() {}

    public void appExit() {}

    public String getAppName()
    {
        return app_name;
    }

    public String getAppTitle()
    {
        return app_title;
    }

    public void addDllExtractorToCache( DLLExtractor extractor )
    {

    }

    public void registerPlugin( Plugin plugin )
    {

    }

    public List<Plugin> getRegisteredPlugins()
    {
        return List.of();
    }


    public Plugin getPlugin(String name)
    {
        return null;
    }

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * @param impl_locale the locale the class was originaly implemented
     * eg "de" for german
     */
    public abstract void loadMlM4Class( Object obj, String impl_locale );

    /**
     * load a MlM file for a spacific class
     */
    public abstract void loadMlM4ClassName(String name, String string);

    /**
     * only use this in case of emergency
     *
     * @return the last instace of the root class
     */
    public static Root getLastRoot() {
        return static_root;
    }

    public Path getStorage() {
        return storage;
    }

    public void setStartupArgs(String[] args) {
        startupArgs = args;
    }

    public String[] getStartupArgs() {
        return startupArgs;
    }
}
