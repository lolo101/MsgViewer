package at.redeye.FrameWork.base;

import at.redeye.FrameWork.Plugin.Plugin;
import at.redeye.FrameWork.base.dll_cache.DLLCache;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plugins {
    protected static final Logger logger = LogManager.getLogger(Plugins.class);
    protected final Map<String, Plugin> plugins = new HashMap<>();
    private final DLLCache dll_cache;

    public Plugins(String appName) {
        dll_cache = new DLLCache(appName);
    }

    public void register(Plugin plugin) {
        if (plugins.containsKey(plugin.getName()))
            return;

        if (plugin.isAvailable()) {
            try {
                plugin.initPlugin(this);
                plugins.put(plugin.getName(), plugin);
            } catch (AccessControlException ex) {
                logger.error(ex, ex);
            }
        }
    }

    public List<Plugin> registered() {
        return new ArrayList<>(plugins.values());
    }


    public Plugin getAvailable(String name) {
        Plugin plugin = plugins.get(name);
        return plugin != null && plugin.isAvailable() ? plugin : null;
    }

    public void addDllExtractorToCache(DLLExtractor extractor) {
        dll_cache.addDllExtractor(extractor);
        dll_cache.initEnv();
    }
}
