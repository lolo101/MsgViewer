package net.sourceforge.MSGViewer.factory.mbox;

import at.redeye.FrameWork.base.LocalRoot;
import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import com.google.common.jimfs.Jimfs;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class MBoxWriterViaJavaMailTest {
    @Test
    void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        URI uri = Objects.requireNonNull(MBoxWriterViaJavaMailTest.class.getResource("/danke.msg")).toURI();
        Message msg = new MessageParser(Path.of(uri)).parseMessage();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.mbox");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                MBoxWriterViaJavaMail writer = new MBoxWriterViaJavaMail(null);
                writer.write(msg, outputStream);
            }
            assertThat(Files.lines(testOut)).contains("Subject:  danke ...");
        }
    }

    @Test
    void testIssue124() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        URI uri = Objects.requireNonNull(MBoxWriterViaJavaMailTest.class.getResource("/issue124/testing.msg")).toURI();
        Message msg = new MessageParser(Path.of(uri)).parseMessage();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.mbox");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Root root = new LocalRoot("test");
                MBoxWriterViaJavaMail writer = new MBoxWriterViaJavaMail(root);
                writer.write(msg, outputStream);
            }
            assertThat(Files.lines(testOut)).contains("Content-Disposition: attachment; filename=Behinder.pdf");
        }
    }
}
