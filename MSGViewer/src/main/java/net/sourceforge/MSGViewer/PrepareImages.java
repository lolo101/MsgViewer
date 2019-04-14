package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class PrepareImages {

    private static final Logger logger = Logger.getLogger(PrepareImages.class.getName());

    String base_dir;
    private final String extra;
    List<Attachment> attachments;

    public PrepareImages( String base_dir, Message message )
    {
        this.base_dir = base_dir;

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

    private String replace_src(String s, List<FileAttachment> attached_images) {

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

            String imgsrc = src;

            if (!src.toLowerCase().startsWith("http:") && !src.isEmpty() ) {
                logger.info("HERE");
                imgsrc = "file:/" + extra + base_dir + "/" + src;
                File file = new File( extra + base_dir + "/" + src );

                if( !file.exists() && !attached_images.isEmpty() )
                {
                    if( src.toLowerCase().startsWith("cid:") )
                    {
                        String cid = src.substring(4);

                        for( FileAttachment fatt : attached_images )
                        {
                            if( cid.equals(fatt.getContentId()) ) {

                                imgsrc = "file:/" + extra + base_dir + "/" + getFileName(fatt);
                                attached_images.remove(fatt);
                                break;
                            }
                        }

                    } else {

                        imgsrc = "file:/" + extra + base_dir + "/" + getFileName(attached_images.remove(0));
                    }
                }
            }

            logger.info("image: " + src);

            if (imgsrc != null) {
                ret.append("\"");
                ret.append(imgsrc);
                ret.append("\"");
                ret.append(rest);
            }
        }

        return ret.toString();
    }

    static String getFileName(FileAttachment fatt)
    {
        if( fatt.getFilename() == null || fatt.getFilename().isEmpty() )
            return fatt.getLongFilename();

        return fatt.getFilename();
    }

    public StringBuilder prepareImages(StringBuilder s) {

        List<FileAttachment> attached_images = new ArrayList<>();

        for( Attachment att : attachments )
        {
            if( att instanceof FileAttachment)
            {
                FileAttachment fatt = (FileAttachment) att;

                String mime_type = fatt.getMimeTag();

                logger.info(fatt + " " + mime_type);

                if( mime_type != null && ViewerHelper.is_image_mime_type(mime_type) ) {
                    attached_images.add(fatt);
                }
            }
        }

        Collections.sort(attached_images,new Comparator<FileAttachment>() {

            @Override
            public int compare(FileAttachment o1, FileAttachment o2)
            {
                if( o1.getSubDir() != null &&
                    o2.getSubDir() != null )
                {
                    return o1.getSubDir().compareTo(o2.getSubDir());
                }

                return o1.getFilename().compareTo(o2.getFilename());
            }
        });

        for (int start = 0; (start = findImgTag(s, start)) >= 0;) {

            int end = s.indexOf(">", start);

            if (end < 0) {
                break;
            }

            String res = replace_src(s.substring(start, end), attached_images);

            s.replace(start, end, res);

            start += Math.max(1, res.length());
        }

        return s;
    }
}
