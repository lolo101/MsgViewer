package net.sourceforge.MSGViewer.factory.mbox;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import net.sourceforge.MSGViewer.AttachmentRepository;
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
    private final AttachmentRepository attachmentRepository;

    public MBoxWriterViaJavaMail(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
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
            Path fileName = dumpAttachment(att);
            part.attachFile(fileName.toFile());
            part.setFileName(MimeUtility.encodeText(fileName.getFileName().toString(), "UTF-8", null));
            mp.addBodyPart(part);
        }

        addHeaders(msg, jmsg);

        jmsg.setContent(mp);

        jmsg.writeTo(out);
    }

    private Path dumpAttachment(Attachment att) throws Exception {
        if (att instanceof FileAttachment)
            return dumpFileAttachment((FileAttachment) att);
        if (att instanceof MsgAttachment)
            return dumpMsgAttachment((MsgAttachment) att);
        throw new IllegalArgumentException("Unknown attchment type: " + att.getClass());
    }

    private Path dumpFileAttachment(FileAttachment fatt) throws IOException {
        Path content = attachmentRepository.getTempFile(fatt);

        try (OutputStream fout = Files.newOutputStream(content)) {
            fout.write(fatt.getData());
        }

        return content;
    }

    private Path dumpMsgAttachment(MsgAttachment msgAtt) throws Exception {
        Message attachedMessage = msgAtt.getMessage();
        Path attachedMessagePath = attachmentRepository.getTempFile(msgAtt);
        new MessageSaver(attachmentRepository, attachedMessage).saveMessage(withExtension(attachedMessagePath));
        return attachedMessagePath;
    }

    private static void writeMBoxHeader(Message msg, OutputStream out) throws IOException {
        Date date = msg.getDate();

        String sb = String.format("From %s %s\r\n",
                msg.getFromEmail(),
                date == null ? "" : DateHeader.date_format.format(date));

        out.write(sb.getBytes(StandardCharsets.US_ASCII));
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

    private Path withExtension(Path subMessage) {
        String fileName = subMessage.getFileName().toString();
        int extensionPosition = fileName.lastIndexOf('.');
        String fileNameWithoutExtension = extensionPosition > 0 ? fileName.substring(0, extensionPosition) : fileName;
        Path parent = subMessage.getParent();
        return parent.resolve(fileNameWithoutExtension + getExtension());
    }

    protected String getExtension() {
        return ".mbox";
    }
}
