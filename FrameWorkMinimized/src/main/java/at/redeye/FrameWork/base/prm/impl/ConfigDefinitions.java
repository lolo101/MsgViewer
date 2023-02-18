package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class ConfigDefinitions {

    public static Map<String, DBConfig> entries = new TreeMap<>();
    public static Collection<String> help_search_path = new Vector<>();

    public static void add(DBConfig config) {
        entries.put(config.getConfigName(), config);
    }

    public static DBConfig get(String name) {
        return entries.get(name);
    }

    public static void add_help_path(String path) {
        help_search_path.add(path);
    }
}
