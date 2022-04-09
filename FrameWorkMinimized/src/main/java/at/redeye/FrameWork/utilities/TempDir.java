package at.redeye.FrameWork.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempDir {

    /**
     * creates a temporary directory. The directory and it's
     * content will be automatically deleted after exit of the virtual machine.
     *
     * @return the path to the directory
     */
    public static Path getTempDir() throws IOException {
        Path tmpfile = Files.createTempDirectory("msgviewer-");
        tmpfile.toFile().deleteOnExit();
        return tmpfile;
    }

}
