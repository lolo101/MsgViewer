package at.redeye.FrameWork.base.dll_cache;

import at.redeye.FrameWork.base.Setup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class DLLCache {
    private static final Logger logger = LogManager.getLogger(DLLCache.class);

    private final String cache_dir;

    // das ist absichtlich ein Vector; eben wegen Sychronized
    private final Collection<DLLExtractor> extractors = new ArrayList<>();

    public DLLCache(String appName) {
        cache_dir = Setup.getAppConfigDir(appName + "/jar/dll_cache");
    }

    synchronized public void initEnv() {
        for (DLLExtractor extractor : extractors) {
            String env = extractor.getPropertyNameForDllDir();

            logger.debug(env + "=" + cache_dir);

            try {
                System.setProperty(env, cache_dir);
            } catch (SecurityException ex) {
                logger.debug("System.setProperty not allowed", ex);
            }
        }
    }

    synchronized public void addDllExtractor( DLLExtractor extractor )
    {
        extractors.add(extractor);
    }
}
