package at.redeye.FrameWork.utilities;

import java.io.File;
import java.io.IOException;

public class TempDir
{
    /**
     * creates a temporary directory
     * prefix and suffix maybe null
     *
     * @return the path to the directory on success, or null. The directory and it's
     * content will be automatically deleted after exit of the virtual machine.
     */
    public static File getTempDir() throws IOException {
        File tmpfile = File.createTempFile("xxxx", null);

        if (!tmpfile.delete())
            return null;

        if (!tmpfile.mkdirs())
            return null;

        tmpfile.deleteOnExit();

        return tmpfile;
    }

}
