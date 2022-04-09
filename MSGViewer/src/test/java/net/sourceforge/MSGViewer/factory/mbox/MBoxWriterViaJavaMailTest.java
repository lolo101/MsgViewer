package net.sourceforge.MSGViewer.factory.mbox;

import com.auxilii.msgparser.Message;
import com.google.common.jimfs.Jimfs;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class MBoxWriterViaJavaMailTest {
    @Test
    public void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        URI uri = Objects.requireNonNull(MBoxWriterViaJavaMailTest.class.getResource("/danke.msg")).toURI();
        Message msg = new MessageParser(Path.of(uri)).parseMessage();

        Path testOut = Jimfs.newFileSystem().getPath("test_out.mbox");
        try (OutputStream outputStream = Files.newOutputStream(testOut)) {
            MBoxWriterViaJavaMail writer = new MBoxWriterViaJavaMail();
            writer.write(msg, outputStream);
        }
        assertThat(Files.lines(testOut)).contains("Subject:  danke ...");
    }
}
