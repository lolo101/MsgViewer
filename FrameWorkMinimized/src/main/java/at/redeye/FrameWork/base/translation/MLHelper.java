/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author martin
 */
public class MLHelper
{
    Root root;
    Properties props;
    String current_lang;
    String locale;
    Properties missing_props;
    String missing_props_file_name;

    List<String> loaded_object_files;

    public MLHelper( Root root )
    {
        this.root = root;
        locale = root.getDisplayLanguage();
        autoLoadCurrentLocale();
    }

    boolean loadPropsFile( String lang ) throws FileNotFoundException, IOException
    {
        boolean loaded_something = false;

        String dir = TranslationDialog.getTranslationsDir(root);
        String file_name = "trans";
        String base_name = dir + file_name;
        String prop = ".properties";

        String extra = "_";

        if( lang.isEmpty() )
            extra = "";

        File dir_exact = new File( base_name + extra + lang + prop );

        props = new Properties();
        current_lang = lang;

        if( dir_exact.isFile() )
        {
            FileInputStream in = new FileInputStream(dir_exact);
            props.load(in);
            in.close();
            loaded_something = true;
        }

        String resourcePath = root.getLanguageTranslationResourcePath();

        if( resourcePath != null )
        {
            String resource_name = "/" + resourcePath + "/trans" + extra + lang + prop;

            resource_name = resource_name.replaceAll("//", "/");

            InputStream in = this.getClass().getResourceAsStream(resource_name);

            if( in != null )
            {
                props.load(in);
                in.close();
                loaded_something = true;
            }
        }

        return loaded_something;
    }

    public boolean loadTrans(String trans) {
        try {
            return loadPropsFile(trans);

        } catch (FileNotFoundException ex) {
            return false;

        } catch (IOException ex) {
            return false;
        }
    }

    public void autoLoadCurrentLocale()
    {
        /*
        if( current_lang != null && locale.equals(current_lang) )
            return;
        */

        if (loadTrans(locale)) {
            return;
        }

        if (locale.length() == 2  && !MLUtil.compareLanguagesOnly(locale, root.getBaseLanguage()) ) {
            loadTrans(root.getDefaultLanguage());
            return;
        }

        if (loadTrans(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if( !MLUtil.compareLanguagesOnly(locale, root.getBaseLanguage()) )
        {
            if( !loadTrans(root.getDefaultLanguage()) )
            {
                // damit nun alle Werte auf das richtige locale eingestellt sind.
                loadTrans(locale);
            }
        }
    }

    private String getMissingPropsFileName()
    {
        if (missing_props_file_name == null)
        {
            String dir = TranslationDialog.getTranslationsDir(root);
            String file_name = "trans";
            String base_name = dir + file_name;
            String prop = ".missing.properties";

            String extra = "_";

            missing_props_file_name = base_name + extra + current_lang + prop;
        }
        return missing_props_file_name;
    }

    private void addMissing( String message )
    {
        if( missing_props == null )
        {
            missing_props = new Properties();
            File file = new File( getMissingPropsFileName() );

            if( file.isFile() )
            {
                try {
                    FileInputStream in = new FileInputStream( file );
                    if( in != null )
                    {
                        missing_props.load(in);
                        in.close();
                    }
                } catch( IOException ex ) {

                }
            }
        }

        missing_props.setProperty(message, "");
    }

    public void saveMissingProps()
    {
        if( missing_props == null )
            return;

        new AutoLogger(MLHelper.class.getName()) {

            @Override
            public void do_stuff() throws Exception {

                FileOutputStream out = new FileOutputStream(getMissingPropsFileName());
                missing_props.store(out, "Untranslated messages");
                out.close();

            }
        };

    }

    /**
     * automatically loads a translation file for the given object.
     * as implementation language root.getBaseLanguage() is used
     * and as target language the current language is used.
     * @param object
     */
    public void autoLoadFile4Class(Object object)
    {
        autoLoadFile4ClassName(object.getClass().getName());
    }

    /**
     * automatically loads a translation file for the given object.
     * as implementation language root.getBaseLanguage() is used
     * and as target language the current language is used.
     * @param object
     */
    public void autoLoadFile4ClassName(String name)
    {
        if( already_loaded( name, current_lang, root.getBaseLanguage() ) )
           return;

        Properties p = MLUtil.autoLoadFile4ClassName(root, name, current_lang, root.getBaseLanguage());

        if( p != null )
        {
            MLUtil.addAllProps(props, p);
        }
    }

    private boolean already_loaded( String name, String locale, String impl_language )
    {
        String key = name + locale + impl_language;

        if( loaded_object_files == null )
             loaded_object_files = new LinkedList<>();

        if( loaded_object_files.contains(key) )
             return true;

        loaded_object_files.add(key);

        return false;
    }


    /**
     * tries locating and loading a translation class for a specific language
     * @param object for which a translation is required
     * @param locale language that is requested eg: "de_AT", or "de"
     * @param impl_language naitive languague shoudl be like "de"
     */
    public void autoLoadFile4ClassName( String name, String locale, String impl_language)
    {
        if( already_loaded( name, locale, impl_language ) )
           return;

        Properties p = MLUtil.autoLoadFile4ClassName(root, name, locale, impl_language);

        if( p != null )
        {
            MLUtil.addAllProps(props, p);
        }
    }

    /**
     * tries locating and loading a translation class for a specific language
     * @param object for which a translation is required
     * @param locale language that is requested eg: "de_AT", or "de"
     * @param impl_language naitive languague shoudl be like "de"
     */
    public void autoLoadFile4Class( Object object, String locale, String impl_language)
    {
        autoLoadFile4ClassName( object.getClass().getName(), locale, impl_language );
    }

    public String MlM( String message )
    {
        if( !MLUtil.shouldBeTranslated( message ) )
            return message;

        if( props != null )
        {
            String msg = props.getProperty(message);

            if( msg == null ||
                msg.trim().isEmpty() )
            {
                addMissing( message );
                return message;
            }

            return msg;
        }

        return message;
    }

}
