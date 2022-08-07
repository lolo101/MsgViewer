package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.BaseDialogBaseHelper;
import at.redeye.FrameWork.base.Root;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class TranslationHelper {
    final BaseDialogBase base_dlg;
    final Root root;
    ExtractStrings extract_strings;
    Properties currentProps;
    List<String> additional_strings;
    boolean tried_autoloading_locale = false;
    final BaseDialogBaseHelper helper;

    class OpenTransDialog implements Runnable {
        public void run() {

            if (extract_strings != null) {
                if (additional_strings != null)
                    extract_strings.strings.addAll(additional_strings);

                // alle in der property Datei gefundenen Strings hinzufügen
                if (currentProps != null) {
                    Set<Object> keys = currentProps.keySet();

                    for (Object key : keys) extract_strings.strings.add((String) key);
                }
            }


            base_dlg.invokeDialogUnique(
                    new TranslationDialog(root, base_dlg.getContainer(), base_dlg.getClass().getName(), extract_strings)
            );
        }
    }

    class SwitchTrans_DE_EN implements Runnable {
        static final String lang_a = "";
        static final String lang_b = "en";
        String lang_current;

        public void run() {
            if (lang_b.equals(lang_current)) {
                lang_current = lang_a;
            } else {
                lang_current = lang_b;
            }

            new AutoLogger<>(this.getClass().getName(), () -> switchTranslation(lang_current)).run();
        }
    }

    public TranslationHelper(Root root, BaseDialogBase base_dlg, BaseDialogBaseHelper helper)
    {
        this.root = root;
        this.base_dlg = base_dlg;
        this.helper = helper;

        helper.registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), new OpenTransDialog() );
        helper.registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), new SwitchTrans_DE_EN() );
    }

    private boolean loadTranslation(String new_trans) {
        return switchTranslation(new_trans, true);
    }

    private boolean switchTranslation(String new_trans) {
        return switchTranslation(new_trans, false);
    }

    private boolean switchTranslation( String new_trans, boolean load_only ) {
        boolean found = true;

        Properties props = MLUtil.autoLoadFile4Class(root, base_dlg, new_trans, true);

        if( props == null ) {
            found = false;
            props = new Properties();
        }

        if (!load_only) {
            if (extract_strings == null) {
                extract_strings = new ExtractStrings(base_dlg.getContainer());
            }

            Map<String, List<JComponent>> all = extract_strings.getComponents();

            Set<String> keys = all.keySet();

            for (String key : keys) {
                String value = props.getProperty(key);

                for (JComponent comp : all.get(key)) {
                    if (value != null && !value.isEmpty()) {
                        assign(comp, value);
                    } else {
                        assign(comp, root.MlM(key));
                    }
                }
            }
        }

        currentProps = props;

        return found;
    }

    private static void assign( JComponent comp, String value )
    {
        ExtractStrings.assign( comp, value );
    }

    public void autoSwitchToCurrentLocale()
    {
        String locale = root.getDisplayLanguage();

        if (locale.equals(helper.getBaseLanguage())) {
            return;
        }

        if (switchTranslation(locale)) {
            return;
        }

        if (locale.length() == 2 && !MLUtil.compareLanguagesOnly(root.getDefaultLanguage(), helper.getBaseLanguage())) {
            switchTranslation(root.getDefaultLanguage());
            return;
        }

        if (switchTranslation(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if( MLUtil.compareLanguagesOnly(locale, helper.getBaseLanguage()))
        {
            return;
        }

        if (!root.getDefaultLanguage().equals(helper.getBaseLanguage())) {
            switchTranslation(root.getDefaultLanguage());
        }
    }

    public void autoLoadCurrentLocale()
    {
        String locale = root.getDisplayLanguage();

        if (locale.equals(helper.getBaseLanguage())) {
            return;
        }

        if (loadTranslation(locale)) {
            return;
        }

        if (locale.length() == 2  && !MLUtil.compareLanguagesOnly(root.getDefaultLanguage(), helper.getBaseLanguage())) {
            loadTranslation(root.getDefaultLanguage());
            return;
        }

        if (loadTranslation(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if( MLUtil.compareLanguagesOnly(locale, helper.getBaseLanguage()))
        {
            return;
        }

        if (!root.getDefaultLanguage().equals(helper.getBaseLanguage())) {
            loadTranslation(root.getDefaultLanguage());
        }
    }

    public String MlM( String message )
    {
        String res = null;

        if( currentProps == null && !tried_autoloading_locale )
        {
            autoLoadCurrentLocale();
            tried_autoloading_locale = true;
        }

        if( currentProps != null )
            res = currentProps.getProperty(message);

        if( res == null )
        {
            if( extract_strings == null )
            {
                // hierher kommen wir, wenn MlM im Konstruktor aufgerufen wird
                // und dadurch autoSwitchToCurrentLocale() noch nicht aufgrufen wird.
                // Die Funktion wird nämlich erst beim doLayout aufgerufen

                if( additional_strings == null )
                    additional_strings = new LinkedList<>();

                additional_strings.add(message);
            }
            else
            {
                // damit im Übersetztungsdialog der Text aufscheint
                extract_strings.strings.add(message);
            }
        }

        if( res == null )
            res = root.MlM(message);

        if( res == null )
           return message;

        return res;
    }
}
