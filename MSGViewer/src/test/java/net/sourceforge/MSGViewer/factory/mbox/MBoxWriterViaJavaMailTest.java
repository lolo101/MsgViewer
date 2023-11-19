package net.sourceforge.MSGViewer.factory.mbox;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import com.google.common.jimfs.Jimfs;
import net.sourceforge.MSGViewer.AttachmentRepository;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MBoxWriterViaJavaMailTest {
    @Test
    void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.mbox");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/danke.msg");
                MBoxWriterViaJavaMail writer = new MBoxWriterViaJavaMail(null);
                writer.write(msg, outputStream);
            }
            assertThat(Files.lines(testOut)).contains("Subject: danke ...");
        }
    }

    @Test
    void testIssue124() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.mbox");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/issue124/testing.msg");
                MBoxWriterViaJavaMail writer = givenWriter();
                writer.write(msg, outputStream);
            }
            assertThat(Files.lines(testOut)).contains("\tfilename=\"Behinderungsanzeige_02 Baustellenfotos-1.pdf\"");
        }
    }

    @Test
    void testIssue127() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.mbox");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/issue127/test.msg");
                MBoxWriterViaJavaMail writer = givenWriter();
                writer.write(msg, outputStream);
            }
            assertThat(Files.lines(testOut)).contains("Content-ID: part1.HRgTI02d.mjRZp5Gh@neuf.fr");
        }
    }

    @Test
    void testIssue133() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.eml");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/issue133/test.msg");
                MBoxWriterViaJavaMail writer = givenWriter();
                writer.write(msg, outputStream);
            }
            assertThat(Files.lines(testOut)).contains("Content-Type: text/rtf;charset=UTF-8");
        }
    }

    @Test
    void testIssue178() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.eml");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage("/danke.msg");
                MBoxWriterViaJavaMail writer = givenWriter();
                writer.write(msg, outputStream);
            }
            assertThat(Files.readString(testOut)).contains("To: \"'Andrea Pirchl'\" <a.pirchl@gmx.at>, \"'Martin Oberzalek'\" <kingleo@gmx.at>");
        }
    }

    @Test
    void testIssue231() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.eml");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = new Message();
                msg.setHeaders("key without value\n");
                MBoxWriterViaJavaMail writer = givenWriter();
                assertThatCode(() -> writer.write(msg, outputStream)).doesNotThrowAnyException();
            }
        }
    }

    private static Message givenMessage(String name) throws Exception {
        URI uri = MBoxWriterViaJavaMailTest.class.getResource(name).toURI();
        return new MessageParser(Path.of(uri)).parseMessage();
    }

    private static MBoxWriterViaJavaMail givenWriter() {
        Root root = new Root("test");
        AttachmentRepository attachmentRepository = new AttachmentRepository(root);
        return new MBoxWriterViaJavaMail(attachmentRepository);
    }
}
