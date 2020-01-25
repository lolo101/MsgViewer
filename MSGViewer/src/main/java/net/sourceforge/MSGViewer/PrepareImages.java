package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepareImages {

    private static final Logger logger = LogManager.getLogger(PrepareImages.class);

    private ViewerHelper viewerHelper;
    List<Attachment> attachments;

    public PrepareImages( ViewerHelper viewerHelper, Message message )
    {
        this.viewerHelper = viewerHelper;

        attachments = message.getAttachments();
    }

    private int findImgTag(StringBuilder s, int start) {
        int pos;

        final Pattern pattern = Pattern.compile("<\\s*img", Pattern.CASE_INSENSITIVE );
        final Matcher matcher = pattern.matcher(s);
        do {
            if( !matcher.find(start) )
                break;

            pos = matcher.start();

            if (s.length() <= pos + 3) {
                return -1;
            }

            if (StringUtils.is_space(s.charAt(pos + 4))) {
                return pos;
            }
        } while (pos < (s.length() - 1));

        return -1;
    }

    private String replace_src(String s, Map<String, FileAttachment> attachmentById, Map<String, FileAttachment> attachmentByLocation) {

        String[] res = s.split("[sS][rR][cC]\\s*=");

        int i = 0;

        StringBuilder ret = new StringBuilder();

        for (String part : res) {
            i++;

            if (i == 1) {
                ret.append(part);
                continue;
            }

            ret.append("src=");

            int start = part.indexOf("\"");
            int end = -1;

            if( start >= 0 )
                end = part.indexOf("\"",start+1);

            String src = part;
            String rest = "";

            if( start >= 0 && end > 0 )
            {
                src = part.substring(start+1,end);
                rest = part.substring(end+1);
            }

            URI imgsrc = URI.create(src);

            FileAttachment fatt = null;
            if (!imgsrc.isAbsolute()) {
                fatt = attachmentByLocation.remove(imgsrc.getPath());
            } else if (imgsrc.getScheme().equals("cid")) {
                fatt = attachmentById.remove(imgsrc.getSchemeSpecificPart());
            }
            if (fatt != null) {
                imgsrc = viewerHelper.getTempFile(fatt).toURI();
            }

            logger.info("image: " + imgsrc);

            ret.append("\"");
            ret.append(imgsrc);
            ret.append("\"");
            ret.append(rest);
        }

        return ret.toString();
    }

    public String prepareImages(StringBuilder s) {

        Map<String, FileAttachment> attachmentById = new HashMap<>();
        Map<String, FileAttachment> attachmentByLocation = new HashMap<>();

        for( Attachment att : attachments )
        {
            if( att instanceof FileAttachment)
            {
                FileAttachment fatt = (FileAttachment) att;
                if (fatt.getContentId() != null) {
                    attachmentById.put(fatt.getContentId(), fatt);
                }
                if (fatt.getContentLocation()!= null) {
                    attachmentByLocation.put(fatt.getContentLocation(), fatt);
                }
            }
        }

        for (int start = 0; (start = findImgTag(s, start)) >= 0;) {

            int end = s.indexOf(">", start);

            if (end < 0) {
                break;
            }

            try {
                String res = replace_src(s.substring(start, end), attachmentById, attachmentByLocation);
                s.replace(start, end, res);
                start += Math.max(1, res.length());
            } catch (RuntimeException rex) {
                logger.error("Failed parsing image tag :", rex);
                start = end;
            }

        }

        return s.toString();
    }
}
