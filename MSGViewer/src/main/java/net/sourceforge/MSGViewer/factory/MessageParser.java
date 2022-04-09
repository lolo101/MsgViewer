package net.sourceforge.MSGViewer.factory;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import net.sourceforge.MSGViewer.factory.mbox.JavaMailParser;

import java.io.IOException;
import java.nio.file.Path;

public class MessageParser {

    private final Path file;

    public MessageParser(Path file) {
        this.file = file;
    }

    public Message parseMessage() throws Exception {
        FileExtension extention = new FileExtension(file);

        switch (extention.toString()) {
            case "msg":
                return parseMsgFile();
            case "mbox":
            case "eml":
                return parseJavaMailFile();
            default:
                throw new Exception("Extension '" + extention + "' not supported");
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
