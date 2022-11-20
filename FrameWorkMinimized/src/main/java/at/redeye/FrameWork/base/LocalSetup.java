package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class LocalSetup extends Setup {

    private String config_file;
    private Properties props;

    public LocalSetup(String app_name) {

        String config_name = app_name + ".properties";

        config_file = getHiddenUserHomeFileName(config_name);

        String new_location = getAppConfigFile(app_name, config_name);

        File file = new File(new_location);

        if (file.exists()) {
            config_file = new_location;
            check();
        } else {
            check();
            config_file = new_location;
            saveConfig();
        }
    }

    private void check() {
        if (props == null) {
            loadProps();
        }
    }

    private void loadProps() {
        props = new Properties();

        try (FileInputStream in = new FileInputStream(config_file)) {
            props.load(in);

        } catch (FileNotFoundException ignored) {


        } catch (IOException ioe) {

            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
    }

    public boolean saveProps() {
        try {
            Properties oldProps = new Properties();

            try (FileInputStream in = new FileInputStream(config_file)) {
                oldProps.load(in);
            } catch (FileNotFoundException ex) {
                logger.error("File " + config_file + " existiert noch nicht.");
            }

            Set<String> keys = oldProps.stringPropertyNames();

            for (String currKey : keys) {
                DBConfig c = LocalConfigDefinitions.get(currKey);
                if (c != null) {
                    PrmActionEvent event = new PrmActionEvent();
                    event.setOldPrmValue(oldProps.getProperty(currKey, ""));
                    event.setNewPrmValue(props.getProperty(currKey, ""));
                    event.setParameterName(currKey);
                    event.setPossibleVals(c.getPossibleValues());
                    c.updateListeners(event);
                }
            }

            FileOutputStream out = new FileOutputStream(config_file);
            props.store(out, "nix");
            out.close();

        } catch (IOException ioe) {

            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getLocalConfig(String key, String default_value) {
        check();
        return props.getProperty(key, default_value);
    }

    @Override
    public String getConfig(String key, String default_value) {
        return default_value;
    }

    @Override
    public void setLocalConfig(String key, String value,
                               boolean only_if_not_exists) {

        check();

        if (props.getProperty(key) == null && only_if_not_exists) {
            props.setProperty(key, value);

        } else if (!only_if_not_exists) {

            props.setProperty(key, value);
        }
    }

    @Override
    public void setLocalConfig(String key, String value) {
        setLocalConfig(key, value, false);
    }

    @Override
    public void setConfig(String key, String value, boolean if_not_exists) {
    }

    @Override
    public void setConfig(String key, String value) {
        setConfig(key, value, false);
    }

    @Override
    public final void saveConfig() {
        saveProps();

    }

    @Override
    public DBConfig getConfig(String key) {
        return null;
    }

    @Override
    public DBConfig getLocalConfig(String key) {
        return LocalConfigDefinitions.get(key);
    }

}
