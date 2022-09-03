package net.sourceforge.MSGViewer.factory.mbox;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import net.sourceforge.MSGViewer.HtmlFromRtf;
import net.sourceforge.MSGViewer.factory.MessageSaver;
import net.sourceforge.MSGViewer.factory.mbox.headers.DateHeader;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static java.util.Objects.requireNonNullElse;

public class MBoxWriterViaJavaMail {
    private final Session session = Session.getInstance(System.getProperties());
    private final Root root;

    public MBoxWriterViaJavaMail(Root root) {
        this.root = root;
    }

    public void write(Message msg, OutputStream out) throws Exception {
        Part jmsg = new MimeMessage(session);

        writeMBoxHeader(msg, out);

        MimeMultipart mp = new MimeMultipart();

        MimeMultipart mp_alternate = new MimeMultipart("alternative");

        String rtf = msg.getBodyRTF();

        if (rtf != null && !rtf.isEmpty()) {
            MimeBodyPart html_text = new MimeBodyPart();

            String html = rtf;

            if (rtf.contains("\\fromhtml")) {
                HtmlFromRtf rtf2html = new HtmlFromRtf(rtf);

                html = rtf2html.getHTML();
            }
            html_text.setDataHandler(new DataHandler(new ByteArrayDataSource(html, "text/html")));

            mp_alternate.addBodyPart(html_text);
        }
        {
            MimeBodyPart plain_text = new MimeBodyPart();
            String plain_text_string = msg.getBodyText();

            plain_text.setText(requireNonNullElse(plain_text_string, ""));

            mp_alternate.addBodyPart(plain_text);

            MimeBodyPart part = new MimeBodyPart();
            part.setContent(mp_alternate);

            mp.addBodyPart(part);

        }

        for (Attachment att : msg.getAttachments()) {
            MimeBodyPart part = new MimeBodyPart();
            part.setDisposition(BodyPart.ATTACHMENT);
            if (att instanceof FileAttachment) {
                FileAttachment fatt = (FileAttachment) att;
                part.setFileName(MimeUtility.encodeText(fatt.toString(), "UTF-8", null));
                part.attachFile(dumpAttachment(fatt).toFile());
            } else if (att instanceof MsgAttachment) {
                MsgAttachment msgAtt = (MsgAttachment) att;
                part.attachFile(dumpAttachment(msgAtt).toFile());
            }
            mp.addBodyPart(part);
        }

        addHeaders(msg, jmsg);

        jmsg.setContent(mp);

        jmsg.writeTo(out);
    }

    private Path dumpAttachment(FileAttachment fatt) throws IOException {
        Path content = root.getStorage().resolve(fatt.getFilename());

        try (OutputStream fout = Files.newOutputStream(content)) {
            fout.write(fatt.getData());
        }

        return content;
    }

    private Path dumpAttachment(MsgAttachment msgAtt) throws Exception {
        Message message = msgAtt.getMessage();

        String message_file_name = message.getSubject();
        if (message_file_name == null || message_file_name.isEmpty()) {
            message_file_name = String.valueOf(message.hashCode());
        }
        message_file_name = message_file_name.replace("/", " ");

        Path subMessage = root.getStorage().resolve(message_file_name + "." + getExtension());
        new MessageSaver(root, message).saveMessage(subMessage);
        return subMessage;
    }

    private static void writeMBoxHeader(Message msg, OutputStream out) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("From ");
        sb.append(msg.getFromEmail());
        sb.append(" ");

        Date date = msg.getDate();

        if (date == null) {
            date = new Date(0);
        }

        sb.append(DateHeader.date_format.format(date));
        sb.append("\r\n");

        out.write(sb.toString().getBytes(StandardCharsets.US_ASCII));
    }

    private static void addHeaders(Message msg, Part jmsg) throws MessagingException {
        if (msg.getHeaders() == null) {
            return;
        }

        String[] headers = msg.getHeaders().split("\n");

        StringBuilder sb = new StringBuilder();

        for (String hl : headers) {
            String header_line = hl.trim();

            if (header_line.startsWith(" ")) {
                sb.append("\n");
                sb.append(header_line);
            } else {
                sb.append(header_line);

                String h = sb.toString();

                int idx = h.indexOf(':');

                if (idx > 0) {
                    String name = h.substring(0, idx);
                    String value = h.substring(idx + 1);

                    if (name.startsWith("From ")) {
                        sb.setLength(0);
                        continue;
                    }
                    jmsg.addHeader(name, value);
                }

                sb.setLength(0);
            }
        }
    }

    public String getExtension() {
        return "mbox";
    }
}
