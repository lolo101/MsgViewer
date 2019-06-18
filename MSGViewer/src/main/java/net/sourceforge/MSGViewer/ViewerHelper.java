package net.sourceforge.MSGViewer;

import static at.redeye.FrameWork.base.BaseDialog.logger;

import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.DeleteDir;
import at.redeye.FrameWork.utilities.StringUtils;
import at.redeye.FrameWork.utilities.TempDir;
import java.io.File;
import java.io.IOException;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.sourceforge.MSGViewer.rtfparser.ParseException;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.apache.poi.util.IOUtils;

public class ViewerHelper {

    private Root root;
    private File tmp_dir;
    boolean delete_tmp_dir = false;

    public ViewerHelper( Root root )
    {
        this.root = root;

        try {
           tmp_dir = TempDir.getTempDir(null, null);
           delete_tmp_dir = true;
        } catch (IOException ex) {
           tmp_dir = new File(System.getProperty("java.io.tmpdir") + "/" + root.getAppName());
        }
    }

   static boolean is_image_mime_type( String mime )
    {
        switch (mime) {
            case "image/jpeg":
            case "image/gif":
            case "image/png":
                return true;
            default:
                return false;
        }
    }

    static boolean is_mail_message( String file_name )
    {
        return file_name.toLowerCase().endsWith(".mbox")
                || file_name.toLowerCase().endsWith(".msg");
    }

    static boolean is_mail_message( String file_name, String mime )
    {

        return is_mail_message( file_name );
    }

    public String getOpenCommand()
    {
        if( Setup.is_win_system() )
            return "explorer";

        return root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.OpenCommand);
    }

    static String prepareText( String s )
    {
        if( s == null )
            return "";

        StringBuilder sb = new StringBuilder();

        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");

        int last_start = 0;

        while( true )
        {
            int start = s.indexOf("http://", last_start);

            if( start == -1 )
            {
                sb.append(s.substring(last_start));
                break;
            }

            logger.info("last_start: " + last_start + " start: " + start + " length: " + s.length() );

            sb.append(s.substring(last_start, start));
            last_start = start;

            sb.append("<a href=\"");

            int i;

            for( i = start; i < s.length(); i++ )
            {
              if( s.indexOf("&gt;",i ) == i )
                  break;

              char c = s.charAt(i);

              if( StringUtils.is_space(c) )
              {
                  break;
              }
              sb.append(c);
            }

            sb.append("\">");
            sb.append(s.substring(start,i-1));
            sb.append("</a>");

            last_start = i;
        }

        return sb.toString();
    }

    static public String stripMetaTags(String html )
    {
       html =  html.replaceAll("<[Mm][eE][Tt][aA]\\s.*>", "");

       StringBuilder body_text = new StringBuilder(html);
       Source source = new Source(html);
       source.fullSequentialParse();

       for( StartTag tag : source.getAllStartTags("font") )
       {
            // remove size="x"
            Attributes atts = tag.getAttributes();
           if( atts == null )
               continue;

           net.htmlparser.jericho.Attribute att = atts.get("size");

           if( att != null )
           {
               int start = att.getBegin();
               int end = att.getEnd();

               for( int i = start; i < end+1; i++ )
               {
                body_text.setCharAt(i, ' ');
               }
           }
       }

       System.out.println(body_text.toString());

       return body_text.toString();
    }


    public void dispose()
    {
         if( delete_tmp_dir && tmp_dir.exists() )
            DeleteDir.deleteDirectory(tmp_dir);
    }

    public String extractHTMLFromRTF(String bodyText, Message message ) throws ParseException
    {
        HtmlFromRtf rtf2html = new HtmlFromRtf(bodyText);

        String html = rtf2html.getHTML();

        html = ViewerHelper.stripMetaTags(html);

        PrepareImages prep_images = new PrepareImages(this, message);

        return prep_images.prepareImages(new StringBuilder(html));
    }

    public File getMailIconFile() throws IOException
    {
        File file = new File(tmp_dir, "mail.png");

        if( file.exists() )
            return file;

        try (InputStream stream = ViewerHelper.class.getResourceAsStream("/icons/rg1024_yellow_mail.png");
                FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(IOUtils.toByteArray(stream));
        }

        return file;
    }

    public File extractUrl(URL url, Message message ) throws IOException
    {
        List<Attachment> attachments = message.getAttachments();

        for( Attachment att : attachments )
        {
            if( att instanceof FileAttachment)
            {
                FileAttachment fatt = (FileAttachment) att;

                String att_file_name = "file://" + URLEncoder.encode(fatt.getFilename(), "utf-8");

                if( att_file_name.equals(url.toString()) )
                {
                    logger.info("opening " + fatt);

                    File content = getTempFile(fatt);

                    if( !content.exists() )
                    {
                        try (FileOutputStream fout = new FileOutputStream(content)) {
                            fout.write(fatt.getData());
                        }
                    }

                    return content;
                }
            }
        }

        return null;
    }

    public File getTempFile(FileAttachment fatt)
    {
        if( !tmp_dir.isDirectory() && !tmp_dir.mkdirs() )
        {
            throw new RuntimeException( "Cannot create tmp dir: " + tmp_dir );
        }
        return new File(tmp_dir,
                org.apache.commons.lang3.StringUtils.isBlank(fatt.getFilename())
                        ? fatt.getLongFilename()
                        : fatt.getFilename());
    }

    public File getTempFile(MsgAttachment matt)
    {
        if( !tmp_dir.isDirectory() && !tmp_dir.mkdirs() )
        {
            throw new RuntimeException( "Cannot create tmp dir: " + tmp_dir );
        }
        return new File(tmp_dir, matt.getMessage().hashCode() + ".msg");
    }

}
