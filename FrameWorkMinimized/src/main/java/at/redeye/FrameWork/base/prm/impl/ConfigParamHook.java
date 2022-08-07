package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.gui.GlobalConfig;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import at.redeye.FrameWork.base.translation.MLUtil;
import at.redeye.FrameWork.widgets.helpwindow.HelpFileLoader;
import at.redeye.FrameWork.widgets.helpwindow.HelpWinHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ConfigParamHook implements HelpWinHook
{
    String keyword;
    Root root;
    boolean global;
    Map<String, DBConfig> config;
    Collection<String> search_path;
    private static final Logger logger = LogManager.getLogger(ConfigParamHook.class);
    String color_even;
    String color_odd;
    String color_title;

    public ConfigParamHook( Root root, String keyword, boolean global, Collection<String> search_path )
    {
        this.keyword = keyword;
        this.root = root;
        this.global = global;
        this.search_path = search_path;

        if( global ) {
            config = GlobalConfigDefinitions.entries;
            root.loadMlM4ClassName(GlobalConfig.class.getName(), "de");
        } else {
            config = LocalConfigDefinitions.entries;
            root.loadMlM4ClassName(LocalConfig.class.getName(), "de");
        }

        Setup setup = root.getSetup();
        color_even = setup.getLocalConfig(FrameWorkConfigDefinitions.HelpParamColorEven);
        color_odd = setup.getLocalConfig(FrameWorkConfigDefinitions.HelpParamColorOdd);
        color_title = setup.getLocalConfig(FrameWorkConfigDefinitions.HelpParamColorTitle);
    }


    public String getKeyword() {
        return keyword;
    }

    public String getText() {

        StringBuilder res = new StringBuilder();

        res.append("<table>\n");
        res.append("<tr bgcolor=\"").append(color_title).append("\"><td><b>");
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

        for (String key : config.keySet()) {
            String color;

            if (count % 2 == 1) {
                color = color_even;
            } else {
                color = color_odd;
            }

            count++;

            res.append("<tr bgcolor=\"").append(color).append("\">\n");

            res.append("<td><font face=\"Verdana\">\n");
            res.append(key);
            res.append("</font></td>\n");

            res.append("<td>\n");
            res.append("<font face=\"Verdana\">\n");

            DBConfig c = config.get(key);
            Setup setup = root.getSetup();
            res.append(global ? setup.getConfig(c) : setup.getLocalConfig(c));

            res.append("</font>\n");
            res.append("</td>\n");

            res.append("<td>\n");
            res.append("<font face=\"Verdana\">\n");
            res.append(c.getConfigValue());
            res.append("</font>\n");
            res.append("</td>\n");

            res.append("<td><font face=\"Verdana\">\n");
            res.append( root.MlM(c.descr.toString()) );
            res.append("</font></td>\n");

            res.append("</tr>\n");


            String extra = findFirstHelp(key);

            if (extra != null && !extra.isEmpty()) {
                res.append("<tr>");
                res.append("<td colspan=4 bgcolor=\"").append(color).append("\">").append("<blockquote>").append("<font face=\"Verdana\">");
                res.append(extra);
                res.append("</blockquote>" +
                        "</font><br/></td> ");
                res.append("</tr>");
            }
        }

        res.append("</table>");

        return res.toString();
    }

    private String findFirstHelp(String key) {
        String extra = null;

        HelpFileLoader hfl = new HelpFileLoader();
        for (String path : search_path) {
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

                if (module_name == null)
                    module_name = key;

                extra = hfl.loadHelp(path, module_name);

            } catch (IOException ex) {
                logger.trace("Hilfemodul: '" + path + "/" + key + ".html' konnte nicht geöffnet werden.");
            }

            if (extra != null && !extra.isEmpty())
                break;
        }
        return extra;
    }

}
