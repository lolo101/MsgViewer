package net.sourceforge.MSGViewer.factory.mbox;

import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Objects;

class MBoxWriterViaJavaMailTest {
    @Test
    public void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        URI uri = Objects.requireNonNull(MBoxWriterViaJavaMailTest.class.getResource("/danke.msg")).toURI();
        Message msg = new MessageParser(new File(uri)).parseMessage();

        try (FileOutputStream outputStream = new FileOutputStream("target/test-classes/test_out.mbox")) {
            MBoxWriterViaJavaMail writer = new MBoxWriterViaJavaMail();
            writer.write(msg, outputStream);
        }
    }
}