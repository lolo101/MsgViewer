package at.redeye.FrameWork.utilities;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {
    @Test
    void should_create_directory() {
        Path tempDir = Storage.getEphemeralStorage("prefix");

        assertTrue(Files.isDirectory(tempDir));
    }
}