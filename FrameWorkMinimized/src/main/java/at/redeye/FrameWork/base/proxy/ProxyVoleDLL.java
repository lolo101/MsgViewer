/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.proxy;

import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author martin
 */
public class ProxyVoleDLL implements DLLExtractor
{
    public static final String LIB_NAME_BASE = "proxy_util_";
    public static final String PROPERTY_NAME = "proxy_vole_lib_dir";

    @Override
    public String getPropertyNameForDllDir() {
        return PROPERTY_NAME;
    }

    @Override
    public void extractDlls() throws IOException
    {
        String envdir = System.getProperty(PROPERTY_NAME);

        for( String lib : getNames() )
        {
            InputStream source = this.getClass().getResourceAsStream("/lib/" + lib);

            if( source == null )
                continue;

            File tempFile = new File( envdir + "/"  + lib );

            FileOutputStream fout = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int read = 0;
            while (read >= 0) {
                fout.write(buffer, 0, read);
                read = source.read(buffer);
            }
            fout.flush();
            fout.close();
            source.close();
        }
    }

    @Override
    public List<String> getNames() {

        List<String> res = new ArrayList<String>();

        if (Setup.is_win_system())
        {
            String LIB_NAME = LIB_NAME_BASE + "w32.dll";
            if (!System.getProperty("os.arch").equals("x86")) {
                LIB_NAME = LIB_NAME_BASE + System.getProperty("os.arch") + ".dll";
            }

            res.add(LIB_NAME);
        }

        return res;
    }

}
