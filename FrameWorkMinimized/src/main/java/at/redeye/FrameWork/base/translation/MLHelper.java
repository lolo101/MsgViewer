package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.Root;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class MLHelper {
    Root root;
    Properties props;
    String current_lang;
    String locale;
    Properties missing_props;
    String missing_props_file_name;

    private final Set<String> loaded_object_files = new HashSet<>();

    public MLHelper(Root root) {
        this.root = root;
        locale = root.getDisplayLanguage();
        autoLoadCurrentLocale();
    }

    boolean loadPropsFile(String lang) throws IOException {
        boolean loaded_something = false;

        String dir = TranslationDialog.getTranslationsDir(root);
        String file_name = "trans";
        String base_name = dir + file_name;
        String prop = ".properties";

        String extra = "_";

        if (lang.isEmpty())
            extra = "";

        File dir_exact = new File(base_name + extra + lang + prop);

        props = new Properties();
        current_lang = lang;

        if (dir_exact.isFile()) {
            FileInputStream in = new FileInputStream(dir_exact);
            props.load(in);
            in.close();
            loaded_something = true;
        }

        String resourcePath = root.getLanguageTranslationResourcePath();

        if (resourcePath != null) {
            String resource_name = "/" + resourcePath + "/trans" + extra + lang + prop;

            resource_name = resource_name.replaceAll("//", "/");

            InputStream in = this.getClass().getResourceAsStream(resource_name);

            if (in != null) {
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
        } catch (IOException ex) {
            return false;
        }
    }

    public void autoLoadCurrentLocale() {
        if (loadTrans(locale)) {
            return;
        }

        if (locale.length() == 2 && !MLUtil.compareLanguagesOnly(locale, root.getBaseLanguage())) {
            loadTrans(root.getDefaultLanguage());
            return;
        }

        if (loadTrans(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if (!MLUtil.compareLanguagesOnly(locale, root.getBaseLanguage())) {
            if (!loadTrans(root.getDefaultLanguage())) {
                // damit nun alle Werte auf das richtige locale eingestellt sind.
                loadTrans(locale);
            }
        }
    }

    private String getMissingPropsFileName() {
        if (missing_props_file_name == null) {
            String dir = TranslationDialog.getTranslationsDir(root);
            String file_name = "trans";
            String base_name = dir + file_name;
            String prop = ".missing.properties";

            String extra = "_";

            missing_props_file_name = base_name + extra + current_lang + prop;
        }
        return missing_props_file_name;
    }

    private void addMissing(String message) {
        if (missing_props == null) {
            missing_props = new Properties();
            File file = new File(getMissingPropsFileName());

            if (file.isFile()) {
                try (FileInputStream in = new FileInputStream(file)) {
                    missing_props.load(in);
                } catch (IOException ignored) {}
            }
        }

        missing_props.setProperty(message, "");
    }

    public void saveMissingProps() {
        if (missing_props == null)
            return;

        new AutoLogger(MLHelper.class.getName(), () -> {
            FileOutputStream out = new FileOutputStream(getMissingPropsFileName());
            missing_props.store(out, "Untranslated messages");
            out.close();
        });

    }

    private boolean already_loaded(String name, String locale, String impl_language) {
        String key = name + locale + impl_language;
        return !loaded_object_files.add(key);
    }


    /**
     * tries locating and loading a translation class for a specific language
     *
     * @param name          for which a translation is required
     * @param locale        language that is requested eg: "de_AT", or "de"
     * @param impl_language naitive languague shoudl be like "de"
     */
    public void autoLoadFile4ClassName(String name, String locale, String impl_language) {
        if (already_loaded(name, locale, impl_language))
            return;

        Properties p = MLUtil.autoLoadFile4ClassName(root, name, locale, impl_language);

        if (p != null) {
            MLUtil.addAllProps(props, p);
        }
    }

    /**
     * tries locating and loading a translation class for a specific language
     *
     * @param object        for which a translation is required
     * @param locale        language that is requested eg: "de_AT", or "de"
     * @param impl_language naitive languague shoudl be like "de"
     */
    public void autoLoadFile4Class(Object object, String locale, String impl_language) {
        autoLoadFile4ClassName(object.getClass().getName(), locale, impl_language);
    }

    public String MlM(String message) {
        if (!MLUtil.shouldBeTranslated(message))
            return message;

        if (props != null) {
            String msg = props.getProperty(message);

            if (msg == null ||
                    msg.trim().isEmpty()) {
                addMissing(message);
                return message;
            }

            return msg;
        }

        return message;
    }

}
