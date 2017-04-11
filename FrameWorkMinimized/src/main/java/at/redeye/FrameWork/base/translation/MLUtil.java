/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author martin
 */
public class MLUtil {

    static Pattern number_pattern = Pattern.compile("^[0-9 \t\\n,\\.]+$");

    public static Properties convertResourceBundleToProperties(ResourceBundle resource) {
        Properties properties = new Properties();

        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, resource.getString(key));
            //System.out.println("key: '" + key + "' value: '" + properties.get(key) + "'");
        }

        return properties;
    }

    public static String getAltResourcePath(String resourceName, String subdir)
    {
        int index = resourceName.lastIndexOf('/');

        return resourceName.substring(0,index) + '/' + subdir + resourceName.substring(index);
    }

    /**
     * Adds all Properties from b to a
     * @param a
     * @param b
     */
    public static void addAllProps( Properties a , Properties b )
    {
        Set<Object> keys = b.keySet();

        Iterator<Object> it = keys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            String val = (String) b.get(key);

            if (!val.isEmpty()) {
                a.setProperty(key, val);
            }
        }
    }

    public static boolean haveResource( String name )
    {
        //System.out.println("testing: " + name);

        Root root = Root.getLastRoot();

        URL url;

        if( root != null )
            url = root.getClass().getResource(name);
        else
            url = name.getClass().getResource(name); // dieser Weg funkt mit Webstart nicht

        // System.out.println("testing: " + name  + (url != null ? " found" : " not found"));

        if( url != null )
            return true;

        return false;
    }

    public static Properties autoLoadFile4Class(Root root, Object object, String locale, boolean no_default)
    {
        return autoLoadFile4ClassName( root, object.getClass().getName(), locale, no_default );
    }

    public static Properties autoLoadFile4ClassName(Root root, String name, String locale, boolean no_default)
    {
        try {
            return loadFile4ClassName(root, name, locale, no_default);
        } catch( FileNotFoundException ex ) {
            return null;
        } catch( IOException ex ) {
            return null;
        }
    }

    private static Properties loadFile4ClassName(Root root, String name, String locale, boolean no_default) throws FileNotFoundException, IOException
    {
        Properties p = null;

        p = loadFile4ClassInt(root, name, locale);

        if( p != null )
            return p;

        String parts[] = locale.split("_");

        if ( parts.length == 1 ) {
            if( !no_default )
                p = loadFile4ClassInt(root, name,root.getDefaultLanguage());
        }

        if( p != null )
            return p;

        p = loadFile4ClassInt(root, name, parts[0]);

        if( p != null )
            return p;

        if( !no_default )
            p = loadFile4ClassInt(root, name,root.getDefaultLanguage());

        return p;
    }

    private static Properties loadFile4ClassInt( Root root, String name, String lang ) throws FileNotFoundException, IOException
    {
        String dir = TranslationDialog.getTranslationsDir(root);

        String file_name =  "/" + name;

        String base_name = dir + file_name;
        String prop = ".properties";

        String extra = "_";

        if( lang.isEmpty() )
            extra = "";

        File dir_exact = new File( base_name + extra + lang + prop );

        String resource_name = "/" + name.replaceAll("\\.", "/") + extra + lang + prop;

        String alt1_resource_name = "/" + MLUtil.getAltResourcePath(name.replaceAll("\\.", "/"), "translations") + extra + lang + prop;
        String alt2_resource_name = "/" + MLUtil.getAltResourcePath(name.replaceAll("\\.", "/"), "resources/translations") + extra + lang + prop;

        Properties local_props = new Properties();

         boolean not_found = false;

        if( dir_exact.isFile() )
        {
            FileInputStream in = new FileInputStream(dir_exact);
            local_props.load(in);
            in.close();

        } else if( haveResource( resource_name ) ) {

            InputStream in = root.getClass().getResourceAsStream( resource_name );
            local_props.load( in );
            in.close();

        } else if( haveResource( alt1_resource_name ) ) {

            InputStream in = root.getClass().getResourceAsStream( alt1_resource_name );
            local_props.load( in );
            in.close();

        } else if( haveResource( alt2_resource_name ) ) {

            InputStream in = root.getClass().getResourceAsStream( alt2_resource_name );
            local_props.load( in );
            in.close();
        } else {
             local_props = null;
        }

         return local_props;
    }

    public static Properties autoLoadFile4Class(Root root, Object object, String locale, String impl_language)
    {
        return autoLoadFile4ClassName( root, object.getClass().getName(), locale, impl_language);
    }

    public static Properties autoLoadFile4ClassName(Root root, String name, String locale, String impl_language)
    {
        try {
            boolean no_default = false;

            if( MLUtil.compareLanguagesOnly(locale, impl_language))
                no_default = true;

            return loadFile4ClassName(root, name, locale, no_default);
        } catch( FileNotFoundException ex ) {
            return null;
        } catch( IOException ex ) {
            return null;
        }
    }

    /**
     * compares the language part of the given locales
     * @param locale_a
     * @param locale_b
     * @return true if both languages are the same
     */
    public static boolean compareLanguagesOnly( String locale_a, String locale_b )
    {
        String a = getLanguageOnly(locale_a);
        String b = getLanguageOnly(locale_b);

        return a.equals(b);
    }

    /**
     * @param locale
     * @return returns "de" when the locale was "de_AT"
     */
    public static String getLanguageOnly( String locale )
    {
        if( locale.length() == 2 )
            return locale;

        return locale.substring(0,2);
    }

    public static boolean shouldBeTranslated(String s)
    {
        if( s.isEmpty() )
            return false;

        if( s.trim().isEmpty() )
            return false;

        Matcher matcher = number_pattern.matcher(s);

        if( matcher.matches() )
            return false;

        return true;
    }
}
