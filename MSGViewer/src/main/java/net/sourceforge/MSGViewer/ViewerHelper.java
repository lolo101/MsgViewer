package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.sourceforge.MSGViewer.rtfparser.ParseException;
import org.apache.commons.validator.routines.EmailValidator;

import javax.activation.MimeType;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static at.redeye.FrameWork.base.BaseDialog.logger;

public class ViewerHelper {

    private static final Pattern META_PATTERN = Pattern.compile("<meta\\s.*>", Pattern.CASE_INSENSITIVE);
    private final Root root;
    private final AttachmentRepository attachmentRepository;

    public ViewerHelper(Root root) {
        this.root = root;
        attachmentRepository = new AttachmentRepository(root);
    }

    static boolean is_image_mime_type(MimeType mime) {
        return mime.getPrimaryType().equalsIgnoreCase("image");
    }

    static boolean is_mail_message(String file_name) {
        return file_name.toLowerCase().endsWith(".mbox")
                || file_name.toLowerCase().endsWith(".msg");
    }

    public String getOpenCommand() {
        if (Setup.is_win_system())
            return "explorer";

        return root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.OpenCommand);
    }

    static public String stripMetaTags(String html) {
        html = META_PATTERN.matcher(html).replaceAll("");

        StringBuilder body_text = new StringBuilder(html);
        Source source = new Source(html);
        source.fullSequentialParse();

        for (StartTag tag : source.getAllStartTags("font")) {
            // remove size="x"
            Attributes atts = tag.getAttributes();
            if (atts == null)
                continue;

            Attribute att = atts.get("size");

            if (att != null) {
                int start = att.getBegin();
                int end = att.getEnd();

                for (int i = start; i < end + 1; i++) {
                    body_text.setCharAt(i, ' ');
                }
            }
        }

        System.out.println(body_text);

        return body_text.toString();
    }


    public String extractHTMLFromRTF(String bodyText, Message message) throws ParseException {
        HtmlFromRtf rtf2html = new HtmlFromRtf(bodyText);

        String html = rtf2html.getHTML();

        html = ViewerHelper.stripMetaTags(html);

        PrepareImages prep_images = new PrepareImages(attachmentRepository, message);

        return prep_images.prepareImages(html);
    }

    public Path extractUrl(URL url, Message message) throws IOException {
        List<Attachment> attachments = message.getAttachments();

        for (Attachment att : attachments) {
            if (att instanceof FileAttachment) {
                FileAttachment fatt = (FileAttachment) att;

                Path content = attachmentRepository.getTempFile(fatt);

                if (content.toUri().toURL().equals(url)) {
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

    private static URI getMailIconFile() {
        try {
            return ViewerHelper.class.getResource("/icons/rg1024_yellow_mail.png").toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
