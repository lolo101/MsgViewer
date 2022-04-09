package at.redeye.FrameWork.utilities;

import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

class DeleteDirTest {

    @Test
    void should_delete_directory_content() {
        Path someDir = Jimfs.newFileSystem().getPath("someDir");
        Path someFile = createFileIn(someDir);
        Assertions.assertTrue(Files.exists(someFile));

        DeleteDir.deleteDirectory(someDir);

        Assertions.assertFalse(Files.exists(someFile));
    }

    private static Path createFileIn(Path someDir) {
        try {
            Files.createDirectories(someDir);
            return Files.createFile(someDir.resolve("someFile"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}