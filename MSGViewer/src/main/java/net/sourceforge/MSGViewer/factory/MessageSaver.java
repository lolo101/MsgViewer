package net.sourceforge.MSGViewer.factory;

import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.factory.mbox.EMLWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.mbox.MBoxWriterViaJavaMail;
import net.sourceforge.MSGViewer.factory.msg.MsgWriter;

import java.io.*;

public class MessageSaver {

    private final Message msg;

    public MessageSaver(Message msg) {
        this.msg = msg;
    }

    public void saveMessage(File file) throws Exception {
        int idx = file.getName().lastIndexOf('.');

        if (idx < 0) {
            throw new FileNotFoundException("Cannot identify file type");
        }

        String suffix = file.getName().substring(idx + 1).toLowerCase();

        switch (suffix) {
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
                throw new Exception("Extension '" + suffix + "' not supported");
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
