package net.sourceforge.MSGViewer.factory;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileExtensionTest {

    @Test
    void should_return_filename_when_no_extension() {
        FileExtension extension = new FileExtension("filename");
        assertEquals("filename", extension.toString());
    }

    @Test
    void should_return_extension_when_present() {
        FileExtension extension = new FileExtension("filename.txt");
        assertEquals("txt", extension.toString());
    }

    @Test
    void should_return_extension_with_path() {
        FileExtension extension = new FileExtension(Path.of("root", "dir", "filename.txt"));
        assertEquals("txt", extension.toString());
    }

    @Test
    void should_return_lowercase_extension() {
        FileExtension extension = new FileExtension("filename.TXT");
        assertEquals("txt", extension.toString());
    }
}
