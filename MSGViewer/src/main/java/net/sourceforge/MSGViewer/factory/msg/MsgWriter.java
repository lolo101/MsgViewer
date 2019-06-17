package net.sourceforge.MSGViewer.factory.msg;

import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;
import com.auxilii.msgparser.Message;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

public class MsgWriter
{
    public void write(Message msg, OutputStream out ) throws IOException
    {
        NPOIFSFileSystem fs = new NPOIFSFileSystem();
        DirectoryNode root = fs.getRoot();

        MsgContainer cont = new MsgContainer(msg);
        cont.write(root);

        fs.writeFilesystem(out);
    }

     public static void main( String args[] )
     {
         ModuleLauncher.BaseConfigureLogging();

         try {
            MessageParserFactory factory = new MessageParserFactory();
            Message msg = factory.parseMessage(new File(
                    "/home/martin/programs/java/MSGViewer/test/data/danke.msg"
                    ));

            MsgWriter writer = new MsgWriter();

            writer.write(msg, new FileOutputStream("/home/martin/programs/java/MSGViewer/test/data/test_out.msg"));

         } catch( Exception ex ) {
             System.out.println(ex);
             ex.printStackTrace();
         }
     }
}
