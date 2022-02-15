package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

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
}
