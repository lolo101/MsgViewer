
package at.redeye.FrameWork.base.prm.impl;


import java.util.TreeMap;
import java.util.Vector;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;

/**
 *
 * @author martin
 */
public class GlobalConfigDefinitions {

    public static TreeMap<String,DBConfig> entries = new TreeMap<String,DBConfig>();
    public static Vector<String> help_search_path = new  Vector<String>();
    
    public static void add( DBConfig config )
    {
        entries.put(config.getConfigName(), config);
    }
    
    public static DBConfig get( String name )
    {
        return entries.get(name);
    }
    
    public static void add_help_path( String path )
    {
        help_search_path.add(path);
    }

}
