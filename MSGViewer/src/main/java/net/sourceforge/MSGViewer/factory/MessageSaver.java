package net.sourceforge.MSGViewer.factory;

import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.factory.mbox.EMLWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.mbox.MBoxWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.msg.MsgWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageSaver {

    private final Message msg;

    public MessageSaver(Message msg) {
        this.msg = msg;
    }

    public void saveMessage(File file) throws Exception {
        FileExtension extension = new FileExtension(file);

        switch (extension.toString()) {
            case "msg":
                saveMsgFile(file);
                break;
            case "mbox":
                saveMBoxFile(file);
                break;
            case "eml":
                saveEMLFile(file);
                break;
            default:
                throw new Exception("Extension '" + extension + "' not supported");
        }
    }

    private void saveMsgFile(File file) throws IOException {
        try (OutputStream stream = new FileOutputStream(file)) {
            MsgWriter msg_writer = new MsgWriter(msg);
            msg_writer.write(stream);
        }
    }

    private void saveMBoxFile(File file) throws Exception {
        try (MBoxWriterViaJavaMail mbox_writer = new MBoxWriterViaJavaMail()) {
            write(mbox_writer, msg, file);
        }
    }

    private void saveEMLFile(File file) throws Exception {
        try (EMLWriterViaJavaMail eml_writer = new EMLWriterViaJavaMail()) {
            write(eml_writer, msg, file);
        }
    }

    private static void write(MBoxWriterViaJavaMail writer, Message msg, File file) throws Exception {
        try (OutputStream stream = new FileOutputStream(file)) {
            writer.write(msg, stream);
        }
    }
}
