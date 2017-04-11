/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import static at.redeye.FrameWork.base.BaseDialog.logger;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.DeleteDir;
import at.redeye.FrameWork.utilities.ReadFile;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;

/**
 *
 * @author martin
 */
public class ViewerHelper {
    
    private Root root;
    private File tmp_dir;
    boolean delete_tmp_dir = false;
    private MessageParserFactory parser_factory = new MessageParserFactory();
    
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
               return true;
           case "image/gif":
               return true;
           case "image/png":
               return true;
       }

        return false;
    }
    
    static boolean is_mail_message( String file_name )
    {
        if( file_name.toLowerCase().endsWith(".mbox") )
            return true;
        
        return file_name.toLowerCase().endsWith(".msg");
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

        int start = 0;
        int last_start = 0;

        while( true )
        {
            start = s.indexOf("http://", last_start);

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
    
    public File getTmpDir()
    {
        return tmp_dir;
    }
    
    public String extractHTMLFromRTF(String bodyText, Message message ) throws ParseException
    {
        if( bodyText.contains("\\purehtml") )
            return bodyText;
        
        HtmlFromRtf rtf2html = new HtmlFromRtf(bodyText);

        String html = rtf2html.getHTML();
        
        html = ViewerHelper.stripMetaTags(html);

        PrepareImages prep_images = new PrepareImages(getTmpDir().getPath(), message);

        return prep_images.prepareImages(new StringBuilder(html)).toString();
    }    
    
    public String getMailIconName( File tmp_dir ) throws IOException
    {
        File file = new File(tmp_dir + "/mail.png");
        
        if( file.exists() )
            return file.toString();
        
        byte bytes[] = ReadFile.getBytesResource(this.getClass(), "/net/sourceforge/MSGViewer/resources/icons/rg1024_yellow_mail.png");
        FileOutputStream writer = new FileOutputStream(file);        
        writer.write(bytes);
        writer.close();
        
        return file.toString();
    }    
    
    public File extractUrl(URL url, Message message ) throws IOException
    {
        List<Attachment> attachments = message.getAttachments();

        for( Attachment att : attachments )
        {
            //System.out.println(att.getClass().getName());
            if( att instanceof FileAttachment)
            {
                FileAttachment fatt = (FileAttachment) att;

                String att_file_name = "file://" + URLEncoder.encode(fatt.toString(),"utf-8");

                // logger.info("file " + fatt);

                if( att_file_name.equals(url.toString()) )
                {
                    logger.info("opening " + fatt);

                    File message_dir = getTmpDir();

                    if( !message_dir.isDirectory() && !message_dir.mkdirs() )
                    {
                        throw new RuntimeException( "Cannot create tmp dir: " + message_dir.getPath() );
                    }

                    File content = new File( message_dir + "/" + fatt.toString() );

                    if( !content.exists() )
                    {
                        FileOutputStream fout = new FileOutputStream(content);

                        fout.write(fatt.getData());

                        fout.close();
                    }

                    return content;
                }
            }
        }

        return null;
    }        
    
    
}
