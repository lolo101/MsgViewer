/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class TempDir
{
    /**
     * creates a temporary directory
     * prefix and suffix maybe null
     * @return the path to the directory on success, or null. The directory and it's
     * content will be automatically deleted after exit of the virtual machine.
     */
    public static File getTempDir(String prefix, String suffix ) throws IOException
    {
        if( prefix == null )
            prefix = "xxxx";

        File tmpfile = File.createTempFile(prefix, suffix);

        if( !tmpfile.delete() )
            return null;

        if( !tmpfile.mkdirs() )
            return null;

        tmpfile.deleteOnExit();

        return tmpfile;
    }

}
