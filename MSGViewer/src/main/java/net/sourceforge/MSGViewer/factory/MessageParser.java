package net.sourceforge.MSGViewer.factory;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import net.sourceforge.MSGViewer.factory.mbox.JavaMailParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MessageParser {

    private final File file;

    public MessageParser(File file) {
        this.file = file;
    }

    public Message parseMessage() throws Exception {
        int idx = file.getName().lastIndexOf('.');

        if (idx < 0) {
            throw new FileNotFoundException("Cannot identify file type");
        }

        String suffix = file.getName().substring(idx + 1).toLowerCase();

        switch (suffix) {
            case "msg":
                return parseMsgFile();
            case "mbox":
            case "eml":
                return parseJavaMailFile();
            default:
                throw new Exception("Extension '" + suffix + "' not supported");
        }
    }

    private Message parseMsgFile() throws IOException {
        MsgParser msg_parser = new MsgParser(file);
        return msg_parser.parseMsg();
    }

    private Message parseJavaMailFile() throws Exception {
        JavaMailParser jmail_parser = new JavaMailParser(file);
        return jmail_parser.parse();
    }
}
