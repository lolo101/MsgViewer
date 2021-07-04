package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.GlobalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;
import at.redeye.FrameWork.base.transaction.Transaction;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class LocalSetup extends Setup {

    String config_file;
    String app_name;
    Properties props;
    Root root;
    HashMap<String, DBConfig> global_config = null;
    boolean initial_run = false;

    public LocalSetup(Root root, String app_name) {
        this.app_name = app_name;
        this.root = root;

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

    private boolean checkGlobal() {
        if (global_config == null) {
            return loadGlobalProps();
        }

        return true;
    }

    public void loadProps() {
        props = new Properties();

        try {
            FileInputStream in = new FileInputStream(config_file);
            props.load(in);
            in.close();

        } catch (FileNotFoundException e) {

            initial_run = true;

        } catch (IOException ioe) {

            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
    }

    public boolean saveProps() {
        try {
            Properties oldProps = new Properties();

            try {
                FileInputStream in = new FileInputStream(config_file);
                oldProps.load(in);
                in.close();
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

    public boolean loadGlobalProps() {
        final DBConnection conn = root.getDBConnection();

        if (conn == null) {
            return false;
        }

        Transaction trans = conn.getNewTransaction();

        List<DBStrukt> all = trans.fetchTable(new DBConfig());

        for (DBStrukt dbStrukt : all) {
            DBConfig c = (DBConfig) dbStrukt;

            if (global_config == null) {
                global_config = new HashMap<>();
            }

            global_config.put(c.getConfigName(), c);
        }

        conn.closeTransaction(trans);

        if (global_config == null) {
            // Noch kein Eintrag in der DB vorhanden...
            global_config = new HashMap<>();
        }

        return true;
    }

    public boolean saveGlobalProps() {
        if (global_config == null)
            return false;

        final DBConnection conn = root.getDBConnection();

        if (conn == null)
            return false;

        AutoLogger al = new AutoLogger("LocalSetup") {

            @Override
            public void do_stuff() {
                result = Boolean.FALSE;

                Transaction trans = conn.getNewTransaction();

                Set<String> keys = global_config.keySet();

                for (String key : keys) {
                    DBConfig c = global_config.get(key);
                    if (c.hasChanged()) {
                        PrmActionEvent event = new PrmActionEvent();
                        event.setParameterName(c.name);
                        event.setOldPrmValue(c.getOldValue());
                        event.setNewPrmValue(c.value);
                        event.setPossibleVals(c.getPossibleValues());
                        DefaultInsertOrUpdater.insertOrUpdateValuesWithPrimKey(
                                trans, c);
                        c.updateListeners(event);
                        c.setChanged(false);
                    }
                }

                trans.commit();
                conn.closeTransaction(trans);

                result = Boolean.TRUE;
            }

        };

        return (Boolean) al.result;
    }

    @Override
    public String getLocalConfig(String key, String default_value) {
        check();
        return props.getProperty(key, default_value);
    }

    @Override
    public String getConfig(String key, String default_value) {

        if (!checkGlobal())
            return default_value;

        DBConfig c = global_config.get(key);

        if (c != null)
            return c.getConfigValue();

        c = GlobalConfigDefinitions.get(key);

        if (c != null) {
            global_config.put(key, c);
            return c.getConfigValue();
        }

        c = new DBConfig(key, default_value);
        global_config.put(key, c);

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

        if (!checkGlobal()) {
            return;
        }

        DBConfig c = global_config.get(key);

        if (c != null && if_not_exists)
            return;

        if (c == null) {
            c = GlobalConfigDefinitions.get(key);

            if (c != null) {
                c.setConfigValue(value);
                c.setChanged();
                global_config.put(key, c);
                return;
            }

            c = new DBConfig(key, value);
            c.setChanged();
            global_config.put(key, c);

        } else {

            c.setChanged();
            c.setConfigValue(value);
        }
    }

    @Override
    public void setConfig(String key, String value) {
        setConfig(key, value, false);
    }

    @Override
    public void saveConfig() {
        saveProps();
        saveGlobalProps();
    }

    @Override
    public DBConfig getConfig(String key) {
        if (!checkGlobal()) {
            return null;
        }
        return (global_config.get(key));
    }

    @Override
    public DBConfig getLocalConfig(String key) {
        return LocalConfigDefinitions.get(key);
    }

    /**
     * @return true if local property file was now created
     */
    @Override
    public boolean initialRun() {
        return initial_run;
    }

}
