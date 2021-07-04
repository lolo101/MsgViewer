package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

class MsgWriterTest {

    @Test
    public void testWrite() throws Exception {
        ModuleLauncher.BaseConfigureLogging();

        try (FileOutputStream outputStream = new FileOutputStream("target/test-classes/test_out.msg")) {
            Message msg = new MessageParser(new File("src/test/resources/danke.msg")).parseMessage();
            new MsgWriter(msg).write(outputStream);
        }
    }
}