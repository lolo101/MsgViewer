package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class PrepareImages {

    private static final Logger logger = Logger.getLogger(PrepareImages.class.getName());

    private ViewerHelper viewerHelper;
    private final String extra;
    List<Attachment> attachments;

    public PrepareImages( ViewerHelper viewerHelper, Message message )
    {
        this.viewerHelper = viewerHelper;

        if( Setup.is_linux_system() )
            extra = "/";
        else
            extra = "";

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

        String res[] = s.split("[sS][rR][cC]\\s*=");

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

                if( end > 0 )
                    rest = part.substring(end+1);
            }

            URI imgsrc = URI.create(src);

            if (!imgsrc.isAbsolute()) {
                FileAttachment fatt = attachmentByLocation.remove(imgsrc.getPath());
                imgsrc = viewerHelper.getTempFile(fatt).toURI();
            } else if (imgsrc.getScheme().equals("cid")) {
                FileAttachment fatt = attachmentById.remove(imgsrc.getPath());
                imgsrc = viewerHelper.getTempFile(fatt).toURI();
            }

            logger.info("image: " + imgsrc);

            if (imgsrc != null) {
                ret.append("\"");
                ret.append(imgsrc);
                ret.append("\"");
                ret.append(rest);
            }
        }

        return ret.toString();
    }

    public StringBuilder prepareImages(StringBuilder s) {

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

        return s;
    }
}
