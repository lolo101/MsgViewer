package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.BaseDialogBaseHelper;
import at.redeye.FrameWork.base.Root;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class TranslationHelper {
    private final BaseDialogBase base_dlg;
    private final Root root;
    private ExtractStrings extract_strings;
    private Properties currentProps;
    private List<String> additional_strings;
    private boolean need_locale_autoload = true;

    class OpenTransDialog implements Runnable {
        @Override
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

    class SwitchTranslation implements Runnable {
        private final String lang_a;
        private final String lang_b;
        private String lang_current;

        private SwitchTranslation() {
            lang_a = root.getDisplayLanguage();
            lang_b = root.getDefaultLanguage();
            lang_current = root.getDisplayLanguage();
        }

        @Override
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

        helper.registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), new OpenTransDialog());
        helper.registerActionKeyListener(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), new SwitchTranslation());
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

            for (Map.Entry<String, List<JComponent>> entry : all.entrySet()) {
                String key = entry.getKey();
                String value = props.getProperty(key);

                for (JComponent comp : entry.getValue()) {
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

        if (locale.equals(root.getBaseLanguage())) {
            return;
        }

        if (switchTranslation(locale)) {
            return;
        }

        if (locale.length() == 2 && !MLUtil.compareLanguagesOnly(root.getDefaultLanguage(), root.getBaseLanguage())) {
            switchTranslation(root.getDefaultLanguage());
            return;
        }

        if (switchTranslation(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if (MLUtil.compareLanguagesOnly(locale, root.getBaseLanguage()))
        {
            return;
        }

        if (!root.getDefaultLanguage().equals(root.getBaseLanguage())) {
            switchTranslation(root.getDefaultLanguage());
        }
    }

    private void autoLoadCurrentLocale() {
        String locale = root.getDisplayLanguage();

        if (locale.equals(root.getBaseLanguage())) {
            return;
        }

        if (loadTranslation(locale)) {
            return;
        }

        if (locale.length() == 2 && !MLUtil.compareLanguagesOnly(root.getDefaultLanguage(), root.getBaseLanguage())) {
            loadTranslation(root.getDefaultLanguage());
            return;
        }

        if (loadTranslation(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if (MLUtil.compareLanguagesOnly(locale, root.getBaseLanguage()))
        {
            return;
        }

        if (!root.getDefaultLanguage().equals(root.getBaseLanguage())) {
            loadTranslation(root.getDefaultLanguage());
        }
    }

    public String MlM( String message )
    {
        String res = null;

        if (currentProps == null && need_locale_autoload) {
            autoLoadCurrentLocale();
            need_locale_autoload = false;
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
