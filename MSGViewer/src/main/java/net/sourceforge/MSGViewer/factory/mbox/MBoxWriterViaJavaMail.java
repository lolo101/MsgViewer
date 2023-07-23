package net.sourceforge.MSGViewer.factory.mbox;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.util.ByteArrayDataSource;
import net.htmlparser.jericho.Source;
import net.sourceforge.MSGViewer.AttachmentRepository;
import net.sourceforge.MSGViewer.ViewerHelper;
import net.sourceforge.MSGViewer.factory.MessageSaver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNullElse;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MBoxWriterViaJavaMail {
    private static final Pattern START_WITH_BLANK = Pattern.compile("^\\s");
    private final Session session = Session.getInstance(System.getProperties());
    private final AttachmentRepository attachmentRepository;

    public MBoxWriterViaJavaMail(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public void write(Message msg, OutputStream out) throws Exception {
        Part jmsg = new MimeMessage(session);

        writeMBoxHeader(msg, out);

        MimeMultipart mp_alternate = new MimeMultipart("alternative");
        addTextPart(msg, mp_alternate);
        addRtfPart(msg, mp_alternate);
        addHtmlPart(msg, mp_alternate);

        MimeBodyPart part = new MimeBodyPart();
        part.setContent(mp_alternate);

        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(part);

        for (Attachment att : msg.getAttachments()) {
            mp.addBodyPart(createPart(att));
        }

        addHeaders(msg, jmsg);

        jmsg.setContent(mp);

        jmsg.writeTo(out);
    }

    protected String getExtension() {
        return ".mbox";
    }

    private BodyPart createPart(Attachment att) throws Exception {
        MimeBodyPart part = new MimeBodyPart();
        part.setDisposition(BodyPart.ATTACHMENT);
        dumpAttachment(att, part);
        return part;
    }

    private void dumpAttachment(Attachment att, MimeBodyPart part) throws Exception {
        if (att instanceof FileAttachment)
            dumpFileAttachment((FileAttachment) att, part);
        else if (att instanceof MsgAttachment)
            dumpMsgAttachment((MsgAttachment) att, part);
        else
            throw new IllegalArgumentException("Unknown attchment type: " + att.getClass());
    }

    private void dumpFileAttachment(FileAttachment fatt, MimeBodyPart part) throws IOException, MessagingException {
        Path content = attachmentRepository.getTempFile(fatt);
        part.attachFile(content.toFile());
        part.setFileName(MimeUtility.encodeText(fatt.toString(), "UTF-8", null));
        part.setContentID(fatt.getContentId());

        try (OutputStream fout = Files.newOutputStream(content)) {
            fout.write(fatt.getData());
        }
    }

    private void dumpMsgAttachment(MsgAttachment msgAtt, MimeBodyPart part) throws Exception {
        Path attachedMessagePath = attachmentRepository.getTempFile(msgAtt);
        part.attachFile(attachedMessagePath.toFile());
        part.setFileName(MimeUtility.encodeText(attachedMessagePath.getFileName().toString(), "UTF-8", null));

        Message attachedMessage = msgAtt.getMessage();
        new MessageSaver(attachmentRepository, attachedMessage).saveMessage(withExtension(attachedMessagePath));
    }

    private static void writeMBoxHeader(Message msg, OutputStream out) throws IOException {
        ZonedDateTime date = msg.getDate();

        String sb = String.format("From %s %s\r\n",
                msg.getFromEmail(),
                date == null ? "" : date.format(Message.DATE_TIME_FORMATTER));

        out.write(sb.getBytes(StandardCharsets.US_ASCII));
    }

    private static void addTextPart(Message msg, MimeMultipart mp_alternate) throws MessagingException {
        String plain_text_string = msg.getBodyText();

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(requireNonNullElse(plain_text_string, ""));
        mp_alternate.addBodyPart(textPart);
    }

    private static void addRtfPart(Message msg, MimeMultipart mp_alternate) throws MessagingException, IOException {
        String rtf = msg.getBodyRTF();

        if (isNotBlank(rtf)) {
            MimeBodyPart rtfPart = new MimeBodyPart();
            rtfPart.setDataHandler(new DataHandler(new ByteArrayDataSource(rtf, "text/rtf;charset=UTF-8")));
            mp_alternate.addBodyPart(rtfPart);
        }
    }

    private static void addHtmlPart(Message msg, MimeMultipart mp_alternate) throws MessagingException {
        byte[] html = msg.getBodyHtml();

        if (html != null) {
            MimeBodyPart htmlPart = new MimeBodyPart();
            Source source = ViewerHelper.toHtmlSource(html);
            htmlPart.setContent(source.toString(), "text/html;charset=" + source.getEncoding());
            mp_alternate.addBodyPart(htmlPart);
        }
    }

    private static void addHeaders(Message msg, Part jmsg) throws MessagingException {
        if (msg.getHeaders() == null) {
            return;
        }

        String[] headers = msg.getHeaders().split("\n");
        Deque<String> lines = new ArrayDeque<>(Arrays.asList(headers));

        while (!lines.isEmpty()) {
            String headerLine = lines.remove();
            int separatorIndex = headerLine.indexOf(':');
            String name = headerLine.substring(0, separatorIndex);
            String value = accumulateValue(lines, headerLine.substring(separatorIndex + 1));
            jmsg.addHeader(name, value);
        }
    }

    private Path withExtension(Path subMessage) {
        String fileName = subMessage.getFileName().toString();
        int extensionPosition = fileName.lastIndexOf('.');
        String fileNameWithoutExtension = extensionPosition > 0 ? fileName.substring(0, extensionPosition) : fileName;
        Path parent = subMessage.getParent();
        return parent.resolve(fileNameWithoutExtension + getExtension());
    }

    private static String accumulateValue(Deque<String> lines, String value) {
        StringBuilder buffer = new StringBuilder(value.trim());
        while (!lines.isEmpty()) {
            String valueContinuation = lines.peek();
            if (START_WITH_BLANK.matcher(valueContinuation).find()) {
                buffer.append(' ').append(valueContinuation.trim());
                lines.remove();
            } else break;
        }
        return buffer.toString();
    }

}
