package net.sourceforge.MSGViewer;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PrepareImages {

    private static final Logger logger = LogManager.getLogger(PrepareImages.class);
    private static final String ATTRIBUTES_GROUP_NAME = "attr";
    private static final Pattern IMG_PATTERN = Pattern.compile("<\\s*img\\s+(?<" + ATTRIBUTES_GROUP_NAME + ">[^>]*)>", Pattern.CASE_INSENSITIVE);
    private static final String SRC_GROUP_NAME = "src";
    private static final Pattern SRC_PATTERN = Pattern.compile("src\\s*=\\s*\"(?<" + SRC_GROUP_NAME + ">[^\"]*)\"", Pattern.CASE_INSENSITIVE);

    private final AttachmentRepository attachmentRepository;
    private final Map<String, FileAttachment> attachmentById = new HashMap<>();
    private final Map<String, FileAttachment> attachmentByLocation = new HashMap<>();

    PrepareImages(AttachmentRepository attachmentRepository, Message message) {
        this.attachmentRepository = attachmentRepository;

        for (Attachment att : message.getAttachments()) {
            if (att instanceof FileAttachment fatt) {
                if (fatt.getContentId() != null) {
                    attachmentById.put(fatt.getContentId(), fatt);
                }
                if (fatt.getContentLocation() != null) {
                    attachmentByLocation.put(fatt.getContentLocation(), fatt);
                }
            }
        }
    }

    String prepareImages(String s) {
        Matcher matcher = IMG_PATTERN.matcher(s);
        StringBuilder ret = new StringBuilder();
        int lastMatchEnd = 0;
        for (; matcher.find(lastMatchEnd); lastMatchEnd = matcher.end(ATTRIBUTES_GROUP_NAME)) {
            try {
                ret.append(s, lastMatchEnd, matcher.start(ATTRIBUTES_GROUP_NAME));
                String img = matcher.group(ATTRIBUTES_GROUP_NAME);
                ret.append(replace_src(img));
            } catch (RuntimeException rex) {
                logger.error("Failed parsing image tag :", rex);
            }
        }
        ret.append(s.substring(lastMatchEnd));
        return ret.toString();
    }

    private String replace_src(String s) {
        Matcher matcher = SRC_PATTERN.matcher(s);
        StringBuilder ret = new StringBuilder();
        int lastMatchEnd = 0;
        for (; matcher.find(lastMatchEnd); lastMatchEnd = matcher.end(SRC_GROUP_NAME)) {
            ret.append(s, lastMatchEnd, matcher.start(SRC_GROUP_NAME));
            String src = matcher.group(SRC_GROUP_NAME);
            ret.append(getImgsrc(src));
        }
        ret.append(s.substring(lastMatchEnd));
        return ret.toString();
    }

    private URI getImgsrc(String src) {
        URI imgsrc = URI.create(src);
        FileAttachment fatt = getFileAttachment(imgsrc);
        return fatt == null ? imgsrc : attachmentRepository.getTempFile(fatt).toUri();
    }

    private FileAttachment getFileAttachment(URI imgsrc) {
        if ("cid".equals(imgsrc.getScheme())) return attachmentById.remove(imgsrc.getSchemeSpecificPart());
        if (!imgsrc.isAbsolute()) return attachmentByLocation.remove(imgsrc.getPath());
        return null;
    }
}
