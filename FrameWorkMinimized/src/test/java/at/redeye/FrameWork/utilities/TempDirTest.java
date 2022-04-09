package at.redeye.FrameWork.utilities;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TempDirTest {
    @Test
    void should_create_existing_directory() throws IOException {
        Path tempDir = TempDir.getTempDir();

        assertTrue(Files.isDirectory(tempDir));
    }
}