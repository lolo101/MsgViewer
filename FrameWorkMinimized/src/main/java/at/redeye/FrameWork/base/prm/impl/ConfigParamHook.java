/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.gui.GlobalConfig;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import at.redeye.FrameWork.base.translation.MLUtil;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;
import at.redeye.FrameWork.widgets.helpwindow.HelpWinHook;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class ConfigParamHook implements HelpWinHook 
{
    String keyword;
    Root root;
    boolean global;
    TreeMap<String, DBConfig> config;
    Collection<String>  search_path;
    private static Logger logger = Logger.getLogger(ConfigParamHook.class.getName());
    String color_even;
    String color_odd;
    String color_title;
    
    public ConfigParamHook( Root root, String keyword, boolean global, Collection<String> search_path )
    {
        this.keyword = keyword;
        this.root = root;
        this.global = global;
        this.search_path = search_path;
       
        if( global )
        {
            config = GlobalConfigDefinitions.entries;
            root.loadMlM4ClassName(GlobalConfig.class.getName(), "de");
        }
        else
        {
            config = LocalConfigDefinitions.entries;
            root.loadMlM4ClassName(LocalConfig.class.getName(), "de");
        }
        
        color_even = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.HelpParamColorEven);
        color_odd = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.HelpParamColorOdd);
        color_title = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.HelpParamColorTitle);        
    }
    
    
    public String getKeyword() {
        return keyword;
    }

    public String getText() {
        
        StringBuilder res = new StringBuilder();
        
        Set<String> keys = config.keySet();
                        
        res.append("<table>\n");
        res.append("<tr bgcolor=\"" + color_title + "\"><td><b>");
        res.append(root.MlM("Parameter"));
        res.append("</b></td>\n");
        res.append("<td><b>");
        res.append(root.MlM("Wert"));
        res.append("</b></td>\n");
        res.append("<td><b>");
        res.append(root.MlM("Standardwert"));
        res.append("</b></td>\n");
        res.append("<td><b>");
        res.append(root.MlM("Beschreibung"));
        res.append("</b></td></tr>\n");
        
        int count = 1;
        
        for( String key : keys )
        {
            String color;
            
            if( count % 2 == 1 )
            {
                color = color_even;
            } else {
                color = color_odd;
            }
            
            count++;
            
            DBConfig c = (DBConfig)config.get(key);
            
            res.append("<tr bgcolor=\"" + color + "\">\n");
            
            res.append("<td><font face=\"Verdana\">\n");
            res.append( key );          
            res.append("</font></td>\n");
            
            res.append("<td>\n");
            res.append("<font face=\"Verdana\">\n");
            
            if( global )
                res.append( root.getSetup().getConfig(c) );
            else
                res.append( root.getSetup().getLocalConfig(c) );
            
            res.append("</font>\n");
            res.append("</td>\n");
            
            res.append("<td>\n");
            res.append("<font face=\"Verdana\">\n");
            res.append( c.getConfigValue());
            res.append("</font>\n");
            res.append("</td>\n");            
                        
            res.append("<td><font face=\"Verdana\">\n");
            res.append( root.MlM(c.descr.toString()) );
            res.append("</font></td>\n");
            
            res.append("</tr>\n");
            
            
            HelpFileLoader hfl = new HelpFileLoader();
            
            String extra = null;
            
            for(String path : search_path)
            {
                try {
                    String locale = root.getDisplayLanguage();

                    String module_name = null;

                    if (MLUtil.haveResource(HelpFileLoader.getResourceName(path, key + "_" + locale))) {
                        module_name = key + "_" + locale;
                    } else if (MLUtil.haveResource(HelpFileLoader.getResourceName(path, key + "_" + MLUtil.getLanguageOnly(locale)))) {
                        module_name = key + "_" + MLUtil.getLanguageOnly(locale);
                    } else {
                        if (!MLUtil.compareLanguagesOnly(root.getBaseLanguage(), root.getDisplayLanguage())) {
                            if (MLUtil.haveResource(HelpFileLoader.getResourceName(path, key + "_" + MLUtil.getLanguageOnly(root.getDefaultLanguage())))) {
                                module_name = key + "_" + MLUtil.getLanguageOnly(root.getDefaultLanguage());
                            }
                        }
                    }
                    
                    if( module_name == null )
                        module_name = key;

                    extra = hfl.loadHelp(path, module_name);

                } catch (IOException ex) {
                    logger.trace("Hilfemodul: '" + path + "/" + key + ".html' konnte nicht geöffnet werden." );
                }
                
                if( extra != null && !extra.isEmpty() )
                    break;
            }
            
            if( extra != null && extra.isEmpty() == false )
            {
                res.append("<tr>");
                res.append("<td colspan=4 bgcolor=\"" + color + "\">" +
                        "<blockquote>" +
                        "<font face=\"Verdana\">");
                res.append(extra);
                res.append("</blockquote>" +
                        "</font><br/></td> ");
                res.append("</tr>");
            }
        }
        
        res.append("</table>");
        
        return res.toString();
    }

}
