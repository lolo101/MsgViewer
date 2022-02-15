package at.redeye.FrameWork.base.translation;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseDialogBase;
import at.redeye.FrameWork.base.BaseDialogBaseHelper;
import at.redeye.FrameWork.base.Root;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;

public class TranslationHelper
{
     BaseDialogBase base_dlg;
     Root  root;
     ExtractStrings extract_strings;
     Properties currentProps;
     List<String> additional_strings;
     boolean tried_autoloading_locale = false;
     BaseDialogBaseHelper helper;

     class OpenTransDialog implements Runnable
     {
         public void run() {

             if( extract_strings != null )
             {
                 if( additional_strings != null )
                    extract_strings.strings.addAll(additional_strings);

                 // alle in der property Datei gefundenen Strings hinzufügen
                 if( currentProps != null )
                 {
                     Set<Object> keys = currentProps.keySet();

                     for (Object key : keys) extract_strings.strings.add((String) key);
                 }
             }


             base_dlg.invokeDialogUnique(
                new TranslationDialog(root, base_dlg.getContainer(), base_dlg.getClass().getName(), extract_strings)
             );
         }
     }

     class SwitchTrans_DE_EN implements Runnable
     {
         String lang_a = "";
         String lang_b = "en";
         String lang_current;

         public void run() {
             if (lang_b.equals(lang_current)) {
                 lang_current = lang_a;
             } else {
                 lang_current = lang_b;
             }

             new AutoLogger(this.getClass().getName(), () -> switchTranslation(lang_current));
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

    private boolean loadTranslation( String new_trans ) throws IOException
    {
        return switchTranslation(new_trans, true);
    }

    private boolean switchTranslation( String new_trans ) throws IOException
    {
        return switchTranslation(new_trans, false);
    }

    private boolean switchTranslation( String new_trans, boolean load_only ) {
        boolean not_found = false;

        Properties props = MLUtil.autoLoadFile4Class(root, base_dlg, new_trans, true);

        if( props == null ) {
            not_found = true;
            props = new Properties();
        }

        if (!load_only) {
            if (extract_strings == null) {
                extract_strings = new ExtractStrings(base_dlg.getContainer());
            }

            HashMap<String, List<JComponent>> all = extract_strings.getComponents();

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

        return !not_found;
    }

    private static void assign( JComponent comp, String value )
    {
        ExtractStrings.assign( comp, value );
    }

    public boolean switchTrans(String trans) {
        try {
            return switchTranslation(trans);
        } catch (IOException ex) {
            return false;
        }
    }

    public boolean loadTrans(String trans) {
        try {
            return loadTranslation(trans);
        } catch (IOException ex) {
            return false;
        }
    }

    public void autoSwitchToCurrentLocale()
    {
        String locale = root.getDisplayLanguage();

        if (locale.equals(helper.getBaseLanguage())) {
            return;
        }

        if (switchTrans(locale)) {
            return;
        }

        if (locale.length() == 2 && !MLUtil.compareLanguagesOnly(root.getDefaultLanguage(), helper.getBaseLanguage())) {
            switchTrans(root.getDefaultLanguage());
            return;
        }

        if (switchTrans(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if( MLUtil.compareLanguagesOnly(locale, helper.getBaseLanguage()))
        {
            return;
        }

        if (!root.getDefaultLanguage().equals(helper.getBaseLanguage())) {
            switchTrans(root.getDefaultLanguage());
        }
    }

    public void autoLoadCurrentLocale()
    {
        String locale = root.getDisplayLanguage();

        if (locale.equals(helper.getBaseLanguage())) {
            return;
        }

        if (loadTrans(locale)) {
            return;
        }

        if (locale.length() == 2  && !MLUtil.compareLanguagesOnly(root.getDefaultLanguage(), helper.getBaseLanguage())) {
            loadTrans(root.getDefaultLanguage());
            return;
        }

        if (loadTrans(MLUtil.getLanguageOnly(locale))) {
            return;
        }

        if( MLUtil.compareLanguagesOnly(locale, helper.getBaseLanguage()))
        {
            return;
        }

        if (!root.getDefaultLanguage().equals(helper.getBaseLanguage())) {
            loadTrans(root.getDefaultLanguage());
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
