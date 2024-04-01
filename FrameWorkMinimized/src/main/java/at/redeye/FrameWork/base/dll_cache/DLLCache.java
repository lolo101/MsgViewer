package at.redeye.FrameWork.base.dll_cache;

import at.redeye.FrameWork.base.Setup;
import java.nio.file.Path;
import java.util.*;
import org.apache.logging.log4j.*;

public class DLLCache {
    private static final Logger logger = LogManager.getLogger(DLLCache.class);

    private final String cache_dir;

    // das ist absichtlich ein Vector; eben wegen Sychronized
    private final Collection<DLLExtractor> extractors = new ArrayList<>();

    public DLLCache(String appName) {
        cache_dir = Setup.getAppConfigDir(Path.of(System.getProperty("user.home")), appName).resolve("jar/dll_cache").toString();
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
