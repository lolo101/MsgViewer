package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MsgWriter {
    public void write(Message msg, OutputStream out) throws IOException {
        NPOIFSFileSystem fs = new NPOIFSFileSystem();
        DirectoryNode root = fs.getRoot();

        MsgContainer cont = new MsgContainer(msg);
        cont.write(root);

        fs.writeFilesystem(out);
    }

    public static void main(String[] args) {
        ModuleLauncher.BaseConfigureLogging();

        try {
            MessageParserFactory factory = new MessageParserFactory();
            Message msg = factory.parseMessage(new File("src/test/resources/danke.msg"));

            MsgWriter writer = new MsgWriter();

            writer.write(msg, new FileOutputStream("src/test/resources/test_out.msg"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
