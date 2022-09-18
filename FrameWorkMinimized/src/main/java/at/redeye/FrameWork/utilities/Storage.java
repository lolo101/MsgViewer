package at.redeye.FrameWork.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Storage {

    /**
     * creates a temporary directory. The directory and it's
     * content will be automatically deleted after exit of the virtual machine.
     *
     * @return the path to the directory
     */
    public static Path getEphemeralStorage(String prefix) {
        Path tmpfile = getTempPath(prefix);
        tmpfile.toFile().deleteOnExit();
        return tmpfile;
    }

    private static Path getTempPath(String prefix) {
        try {
            return Files.createTempDirectory(prefix + "-");
        } catch (IOException e) {
            return Path.of(System.getProperty("java.io.tmpdir"), prefix);
        }
    }
}
