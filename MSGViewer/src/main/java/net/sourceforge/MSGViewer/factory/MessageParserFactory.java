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

public class MessageParserFactory
{
    public Message parseMessage( File file) throws Exception
    {
        int idx = file.getName().lastIndexOf('.');

        if( idx < 0 ) {
            throw new FileNotFoundException("Cannot identify file type");
        }

        String suffix = file.getName().substring(idx+1).toLowerCase();

        switch (suffix) {
            case "msg":
                return parseMsgFile( file );
            case "mbox":
            case "eml":
                return parseJavaMailFile( file );
            default:
                throw new Exception("Extension '" + suffix + "' not supported");
        }
    }

    private Message parseMsgFile( File file ) throws IOException
    {
        MsgParser msg_parser = new MsgParser();
        return msg_parser.parseMsg(file);
    }

    private Message parseJavaMailFile(File file) throws Exception
    {
        JavaMailParser jmail_parser = new JavaMailParser();
        return jmail_parser.parse(file);
    }

    public void saveMessage( Message msg, File file ) throws Exception
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
            default:
                throw new Exception("Extension '" + suffix + "' not supported");
        }
    }

    private void saveMsgFile(Message msg, File file) throws IOException {
        try (OutputStream stream = new FileOutputStream(file)) {
            MsgWriter msg_writer = new MsgWriter();
            msg_writer.write(msg, stream);
        }
    }

    private void saveMBoxFile(Message msg, File file) throws Exception
    {
        MBoxWriterViaJavaMail mbox_writer = new MBoxWriterViaJavaMail();
        write(mbox_writer, msg, file);
    }

    private void saveEMLFile(Message msg, File file) throws Exception
    {
        EMLWriterViaJavaMail eml_writer = new EMLWriterViaJavaMail();
        write(eml_writer, msg, file);
    }

    private void write(MBoxWriterViaJavaMail writer, Message msg, File file) throws Exception {
        try (OutputStream stream = new FileOutputStream(file)) {
            writer.write(msg, stream);
        } finally {
            writer.close();
        }
    }
}
