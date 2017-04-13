package net.sourceforge.MSGViewer.factory;

import net.sourceforge.MSGViewer.factory.mbox.EMLWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.mbox.JavaMailParser;
import net.sourceforge.MSGViewer.factory.mbox.MBoxWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.msg.MsgWriter;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author martin
 */
public class MessageParserFactory
{
    private MsgParser msg_parser;
    private JavaMailParser jmail_parser;
    private MBoxWriterViaJavaMail mbox_writer;
    private EMLWriterViaJavaMail eml_writer;
    private MsgWriter msg_writer;

    public  Message parseMessage( File file) throws FileNotFoundException, IOException, Exception
    {
        int idx = file.getName().lastIndexOf('.');

        if( idx < 0 ) {
            throw new FileNotFoundException("Cannot identify file type");
        }

        String suffix = file.getName().substring(idx+1).toLowerCase();

        switch (suffix) {
            case "msg": return parseMsgFile( file );
            case "mbox": return paserJavaMailFile( file );
            case "eml": return paserJavaMailFile( file );
            default: return null;
        }
    }

    private Message parseMsgFile( File file ) throws IOException
    {
        if( msg_parser == null ) {
            msg_parser = new MsgParser();
        }

        return msg_parser.parseMsg(file);
    }

    private Message paserJavaMailFile(File file) throws IOException, Exception {

        if( jmail_parser == null ) {
            jmail_parser = new JavaMailParser();
        }

        return jmail_parser.parse(file);
    }

    public void saveMessage( Message msg, File file ) throws FileNotFoundException, Exception
    {
        int idx = file.getName().lastIndexOf('.');

        if( idx < 0 ) {
            throw new FileNotFoundException("Cannot identify file type");
        }

        String suffix = file.getName().substring(idx+1).toLowerCase();

        switch (suffix) {
            case "msg": saveMsgFile( msg, file ); break;
            case "mbox": saveMBoxFile( msg, file ); break;
            case "eml": saveEMLFile( msg, file ); break;
        }
    }

    private void saveMsgFile(Message msg, File file) throws IOException {
        if( msg_writer == null ) {
            msg_writer = new MsgWriter();
        }

        try (OutputStream stream = new FileOutputStream(file)) {
            msg_writer.write(msg, stream);
        }
    }

    private void saveMBoxFile(Message msg, File file) throws Exception
    {
        if( mbox_writer == null ) {
            mbox_writer = new MBoxWriterViaJavaMail();
        }

        try (OutputStream stream = new FileOutputStream(file)) {
            mbox_writer.write(msg, stream);
        } catch( Exception ex ) {
            mbox_writer.close();
            throw ex;
        }
    }

    private void saveEMLFile(Message msg, File file) throws Exception
    {
        if( eml_writer == null ) {
            eml_writer = new EMLWriterViaJavaMail();
        }

        try (OutputStream stream = new FileOutputStream(file)) {
            eml_writer.write(msg, stream);
        } catch( Exception ex ) {
            eml_writer.close();
            throw ex;
        }
    }
}
