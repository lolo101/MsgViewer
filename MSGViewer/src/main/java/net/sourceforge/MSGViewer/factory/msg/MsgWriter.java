package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParser;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MsgWriter {
    private final Message msg;

    public MsgWriter(Message msg) {
        this.msg = msg;
    }

    public void write(OutputStream out) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        DirectoryNode root = fs.getRoot();

        MsgContainer cont = new MsgContainer(msg);
        cont.write(root);

        fs.writeFilesystem(out);
    }

    public static void main(String[] args) {
        ModuleLauncher.BaseConfigureLogging();

        try {
            Message msg = new MessageParser(new File("src/test/resources/danke.msg")).parseMessage();
            new MsgWriter(msg).write(new FileOutputStream("src/test/resources/test_out.msg"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
