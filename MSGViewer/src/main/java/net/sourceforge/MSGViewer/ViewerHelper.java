package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import net.htmlparser.jericho.*;
import net.sourceforge.MSGViewer.rtfparser.ParseException;
import org.apache.commons.validator.routines.EmailValidator;

import javax.activation.MimeType;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static at.redeye.FrameWork.base.BaseDialog.logger;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ViewerHelper {

    private final AttachmentRepository attachmentRepository;

    public ViewerHelper(Root root) {
        attachmentRepository = new AttachmentRepository(root);
    }

    static boolean is_image_mime_type(MimeType mime) {
        return mime.getPrimaryType().equalsIgnoreCase("image");
    }

    static boolean is_mail_message(String file_name) {
        return file_name.toLowerCase().endsWith(".mbox")
                || file_name.toLowerCase().endsWith(".msg");
    }

    public static String getOpenCommand() {
        if (Setup.is_win_system())
            return "explorer";

        return FrameWorkConfigDefinitions.OpenCommand.getConfigValue();
    }

    public String extractHTMLFromRTF(Message message, String rtf) throws ParseException {
        HtmlFromRtf rtf2html = new HtmlFromRtf(rtf);
        return prepareImages(message, rtf2html.getHTML());
    }

    public String prepareImages(Message message, byte[] bodyHtml) {
        try (InputStream stream = new ByteArrayInputStream(bodyHtml)) {
            return prepareImages(message, new Source(stream));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Path extractUrl(URI uri, Message message) throws IOException {
        List<Attachment> attachments = message.getAttachments();

        for (Attachment att : attachments) {
            if (att instanceof FileAttachment) {
                FileAttachment fatt = (FileAttachment) att;

                Path content = attachmentRepository.getTempFile(fatt);

                if (content.toUri().equals(uri)) {
                    logger.info("opening " + fatt);

                    if (!Files.exists(content)) {
                        try (OutputStream fout = Files.newOutputStream(content)) {
                            fout.write(fatt.getData());
                        }
                    }

                    return content;
                }
            }
        }

        return null;
    }

    public static boolean isValidEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }

    public static String printMailIconHtml() {
        return "<img border=0 align=\"baseline\" src=\"" + getMailIconFile() + "\"/>";
    }

    private String prepareImages(Message message, Source html) {
        PrepareImages prep_images = new PrepareImages(attachmentRepository, message);
        return prep_images.prepareImages(stripMetaTags(html));
    }

    private static String stripMetaTags(Source html) {
        StringBuilder mutableHtml = new StringBuilder(html);

        html.getAllElements("meta").forEach(element -> removeSegment(mutableHtml, element));
        html.getAllStartTags("font").forEach(tag -> removeSizeAttribute(mutableHtml, tag));

        System.out.println(mutableHtml);
        return mutableHtml.toString();
    }

    private static void removeSizeAttribute(StringBuilder mutableHtml, StartTag tag) {
        Attributes atts = tag.getAttributes();
        if (atts == null) return;

        Attribute att = atts.get("size");
        if (att == null) return;

        removeSegment(mutableHtml, att);
    }

    private static void removeSegment(StringBuilder mutableHtml, Segment segment) {
        int start = segment.getBegin();
        int end = segment.getEnd();
        mutableHtml.replace(start, end, repeat(' ', end - start));
    }

    private static URI getMailIconFile() {
        try {
            return ViewerHelper.class.getResource("/icons/rg1024_yellow_mail.png").toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
