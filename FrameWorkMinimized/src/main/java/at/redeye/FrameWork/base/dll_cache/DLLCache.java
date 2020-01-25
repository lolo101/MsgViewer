/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.dll_cache;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public class DLLCache
{
   private static Logger logger = LogManager.getLogger(DLLCache.class);

   String cache_dir;

   // das ist absichtlich ein Vector; eben wegen Sychronized
   ArrayList<DLLExtractor> extractors = new ArrayList<DLLExtractor>();

    public DLLCache(Root root) {
        cache_dir = Setup.getAppConfigDir(root.getAppName() + "/jar/dll_cache");
    }

    synchronized public void initEnv()
    {
        for( DLLExtractor extractor : extractors )
        {
            String env = extractor.getPropertyNameForDllDir();

            logger.debug(env + "=" + cache_dir);

            try {
                System.setProperty(env, cache_dir);
            } catch( AccessControlException ex ) {
                logger.debug("System.setProperty now allowed",ex);
            }
        }
    }

    synchronized public void addDllExtractor( DLLExtractor extractor )
    {
        extractors.add(extractor);
    }

    /**
     * extracts all required dlls
     */
    public void update()
    {
        for( DLLExtractor extractor : extractors )
        {
            File fcache_dir = new File( cache_dir );

            if( !fcache_dir.exists() )
                fcache_dir.mkdirs();

            for( String dll_name : extractor.getNames() )
            {
                File dll = new File( cache_dir + "/" + dll_name );

                if( !dll.exists() )
                {
                    try
                    {
                        extractor.extractDlls();
                        break;
                    } catch( AccessControlException ex ) {
                        logger.error(StringUtils.exceptionToString(ex));
                    } catch( IOException ex ) {
                        logger.error(StringUtils.exceptionToString(ex));
                    }
                }
            }
        }
    }
}
