package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import com.google.common.jimfs.Jimfs;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.google.common.jimfs.Configuration.unix;
import static org.assertj.core.api.Assertions.assertThat;

class MsgWriterTest {

    @Test
    void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem(unix())) {
            Path testOut = fileSystem.getPath("test_out.msg");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/danke.msg");
                new MsgWriter(msg).write(outputStream);
            }
            assertThat(Files.isRegularFile(testOut)).isTrue();
        }
    }

    @Test
    void issue129() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem(unix())) {
            Path testOut = fileSystem.getPath("test_out.msg");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/issue129/test.eml");
                new MsgWriter(msg).write(outputStream);
            }

            try (POIFSFileSystem fs = new POIFSFileSystem(Files.newInputStream(testOut))) {
                DirectoryNode root = fs.getRoot();
                DirectoryNode attachment = (DirectoryNode) root.getEntry("__attach_version1.0_#00000000");
                DocumentEntry contentId = (DocumentEntry) attachment.getEntry("__substg1.0_3712001F");
                try (DocumentInputStream stream = new DocumentInputStream(contentId)) {
                    String contentIdValue = new String(stream.readAllBytes(), StandardCharsets.UTF_16LE);
                    assertThat(contentIdValue).isEqualTo("part1.HRgTI02d.mjRZp5Gh@neuf.fr");
                }
            }
        }
    }

    private static Message givenMessage(String name) throws Exception {
        URI uri = Objects.requireNonNull(MsgWriterTest.class.getResource(name)).toURI();
        return new MessageParser(Path.of(uri)).parseMessage();
    }
}
