package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.DeleteDir;
import at.redeye.FrameWork.utilities.TempDir;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.sourceforge.MSGViewer.rtfparser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.poi.util.IOUtils;

import javax.activation.MimeType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static at.redeye.FrameWork.base.BaseDialog.logger;

public class ViewerHelper implements AttachmentRepository {

    private static final Pattern META_PATTERN = Pattern.compile("<meta\\s.*>", Pattern.CASE_INSENSITIVE);
    private final Root root;
    private Path tmp_dir;
    private boolean delete_tmp_dir;

    public ViewerHelper(Root root) {
        this.root = root;

        try {
            tmp_dir = TempDir.getTempDir();
            delete_tmp_dir = true;
        } catch (IOException ex) {
            tmp_dir = Path.of(System.getProperty("java.io.tmpdir"), root.getAppName());
        }
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


    public void dispose() {
        if (delete_tmp_dir && Files.isDirectory(tmp_dir))
            DeleteDir.deleteDirectory(tmp_dir);
    }

    public String extractHTMLFromRTF(String bodyText, Message message) throws ParseException {
        HtmlFromRtf rtf2html = new HtmlFromRtf(bodyText);

        String html = rtf2html.getHTML();

        html = ViewerHelper.stripMetaTags(html);

        PrepareImages prep_images = new PrepareImages(this, message);

        return prep_images.prepareImages(html);
    }

    public Path extractUrl(URL url, Message message) throws IOException {
        List<Attachment> attachments = message.getAttachments();

        for (Attachment att : attachments) {
            if (att instanceof FileAttachment) {
                FileAttachment fatt = (FileAttachment) att;

                Path content = getTempFile(fatt);

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

    @Override
    public Path getTempFile(FileAttachment fatt) {
        return getTempFile(StringUtils.isBlank(fatt.getLongFilename()) ? fatt.getFilename() : fatt.getLongFilename());
    }

    @Override
    public Path getTempFile(MsgAttachment matt) {
        return getTempFile(matt.getMessage().hashCode() + ".msg");
    }

    private Path getTempFile(String fileName) {
        try {
            tmp_dir = Files.createDirectories(tmp_dir);
            return tmp_dir.resolve(fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean isValidEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }

    public String printMailIconHtml() throws IOException {
        return "<img border=0 align=\"baseline\" src=\"" + getMailIconFile() + "\"/>";
    }

    private URI getMailIconFile() throws IOException {
        Path file = tmp_dir.resolve("mail.png");

        if (!Files.exists(file)) {
            try (InputStream stream = ViewerHelper.class.getResourceAsStream("/icons/rg1024_yellow_mail.png");
                 OutputStream writer = Files.newOutputStream(file)) {
                IOUtils.copy(stream, writer);
            }
        }

        return file.toUri();
    }
}
