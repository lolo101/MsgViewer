package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;

import java.util.Set;
import java.util.TreeMap;

public class PrmDBInit {

    private final Root root;

    public PrmDBInit(Root root) {
        this.root = root;
    }

    public void initDb() {
        Set<String> keys = LocalConfigDefinitions.entries.keySet();

        TreeMap<String, DBConfig> vals = new TreeMap<>();

        for (String key : keys) {
            vals.put(key, LocalConfigDefinitions.get(key));
        }

        for (String key : keys) {
            DBConfig c = (DBConfig) vals.get(key).getCopy();
            root.getSetup().setLocalConfig(c.getConfigName(), c.getConfigValue(), true);
        }

        root.saveSetup();
    }

}
