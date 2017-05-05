package net.sourceforge.MSGViewer.factory.msg;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import net.sourceforge.MSGViewer.ModuleLauncher;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypInteger32;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypeTime;
import net.sourceforge.MSGViewer.factory.msg.entries.BodyTextEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.HeadersEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.MessageClassEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.StringUTF16SubstgEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.SubjectEntry;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.attachment.Attachment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypBoolean;
import net.sourceforge.MSGViewer.factory.msg.entries.RTFBodyTextEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

/**
 *
 * @author martin
 */
public class MsgWriter
{
    /*
    public void write(Message msg, OutputStream out ) throws IOException
    {
         POIFSFileSystem fs = new POIFSFileSystem();

         DirectoryEntry root = fs.getRoot();

         new SubjectEntry().createEntry(root, msg.getSubject());
         new BodyTextEntry().createEntry(root, msg.getBodyText());
         new MessageClassEntry().createEntry(root); // required
         new HeadersEntry().createEntry(root, msg.getHeaders());

         TopLevelPropertyStream top_props = new TopLevelPropertyStream(root);

         top_props.save();

         fs.writeFilesystem(out);
    }     */

    public void write(Message msg, OutputStream out ) throws IOException
    {
        MsgContainer cont = new MsgContainer();

        if( msg.getSubject() != null ) {
            cont.addVarEntry( new SubjectEntry( msg.getSubject() ) );
        }

        cont.addVarEntry( new MessageClassEntry() );

        if( isNotEmpty(msg.getBodyText()) ) {
            cont.addVarEntry( new BodyTextEntry(msg.getBodyText()) );
        }

        if (isNotEmpty(msg.getBodyRTF())) {
            cont.addVarEntry( new RTFBodyTextEntry(msg.getBodyRTF() ) );
             // RTF in Sync
            cont.addProperty(new PropPtypBoolean("0e1f",true));
        }

        if( msg.getHeaders() != null  && !msg.getHeaders().isEmpty() ) {
            cont.addVarEntry( new HeadersEntry(msg.getHeaders() ) );
        }

        // PidTagStoreSupportMask data is encoded in unicode
        cont.addProperty(new PropPtypInteger32("340d",0x00040000));

        // PidTagCreationTime
        cont.addProperty(new PropPtypeTime("3007", System.currentTimeMillis()));

        // PidTagLastModificationTime
        cont.addProperty(new PropPtypeTime("3008", System.currentTimeMillis()));

        // PidTagClientSubmitTime
        if( msg.getDate() != null ) {
            cont.addProperty(new PropPtypeTime("0039", msg.getDate().getTime()));
        }

        if( msg.getFromName() != null && !msg.getFromName().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("0042", msg.getFromName() ) );
        }

        if( msg.getFromEmail() != null && !msg.getFromEmail().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("0c1f", msg.getFromEmail() ) );
        }

        if( msg.getToEmail() != null && !msg.getToEmail().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("0076", msg.getToEmail() ) );
        }

        if( msg.getToName() != null && !msg.getToName().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("3001", msg.getToName() ) );
        }

        if( msg.getMessageId() != null && !msg.getMessageId().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("1035", msg.getMessageId() ) );
        }

        if( msg.getDisplayTo() != null && !msg.getDisplayTo().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("0e04",msg.getDisplayTo()));
        }

        if( msg.getDisplayCc() != null && !msg.getDisplayCc().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("0e03",msg.getDisplayCc()));
        }

        if( msg.getDisplayBcc() != null  && !msg.getDisplayBcc().isEmpty() ) {
            cont.addVarEntry( new StringUTF16SubstgEntry("0e02",msg.getDisplayBcc()));
        }

        for( RecipientEntry rec_entry : msg.getRecipients() ) {
            cont.addRecipient(rec_entry);
        }

        for (Attachment attachment : msg.getAttachments()) {
            // TODO
        }

        // TopLevelPropertyStream top_props = new TopLevelPropertyStream(root);

        // top_props.save();

        NPOIFSFileSystem fs = new NPOIFSFileSystem();
        DirectoryNode root = fs.getRoot();
        cont.write(root);

        fs.writeFilesystem(out);
    }

     public static void main( String args[] )
     {
         ModuleLauncher.BaseConfigureLogging();

         try {
            MessageParserFactory factory = new MessageParserFactory();
            Message msg = factory.parseMessage(new File(
                    //+ "/home/martin/NetBeansProjects/redeye/MSGViewer/test/data/danke.msg"
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
