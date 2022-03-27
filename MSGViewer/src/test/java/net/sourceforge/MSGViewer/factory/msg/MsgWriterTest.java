package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import com.google.common.jimfs.Jimfs;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MsgWriterTest {

    @Test
    public void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        Path testOut = Jimfs.newFileSystem().getPath("test_out.mbox");
        try (OutputStream outputStream = Files.newOutputStream(testOut)) {
            Message msg = new MessageParser(new File("src/test/resources/danke.msg")).parseMessage();
            new MsgWriter(msg).write(outputStream);
        }
        assertThat(Files.isRegularFile(testOut)).isTrue();
    }
}
