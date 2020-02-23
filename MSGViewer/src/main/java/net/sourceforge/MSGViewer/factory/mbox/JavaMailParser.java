package net.sourceforge.MSGViewer.factory.mbox;

import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import net.sourceforge.MSGViewer.factory.mbox.headers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Enumeration;

/**
 *
 * @author martin
 */
public class JavaMailParser
{
    private static final Logger LOGGER = LogManager.getLogger(JavaMailParser.class);

    private static final EmailHeader FROM_PARSER = new FromEmailHeader();
    private static final EmailHeader TO_PARSER =  new ToEmailHeader();
    private static final EmailHeader CC_PARSER =  new CcEmailHeader();
    private static final EmailHeader BCC_PARSER =  new BccEmailHeader();
    private static final DateHeader DATE_PARSER = new DateHeader();

    public Message parse( File file ) throws Exception
    {
        javax.mail.Message jmsg = parseJMessage(file);

        Message msg = new Message();

        FROM_PARSER.parse(msg, getAddresses(jmsg.getFrom()) );
        TO_PARSER.parse(msg, getAddresses(jmsg.getRecipients(RecipientType.TO)) );
        CC_PARSER.parse(msg, getAddresses(jmsg.getRecipients(RecipientType.CC)) );
        BCC_PARSER.parse(msg, getAddresses(jmsg.getRecipients(RecipientType.BCC)) );
        msg.setSubject(jmsg.getSubject());


        msg.setHeaders(getHeaders(jmsg.getAllHeaders()));
        DATE_PARSER.parse(msg, getFirstHeader(jmsg.getHeader("Date")) );
        msg.setMessageId(getFirstHeader(jmsg.getHeader("Message-Id")));

        msg.setBodyText("");

        parse( msg, jmsg );

        return msg;
    }

    private javax.mail.Message parseJMessage(File file) throws MessagingException, IOException {
        try (InputStream stream = new FileInputStream(file)) {
            Session session = Session.getInstance(System.getProperties());
            return new MimeMessage(session, stream);
        }
    }

    private void parse( Message msg, Part part ) throws MessagingException, IOException
    {
        LOGGER.info("Content Type: " + part.getContentType());

        if( part.isMimeType("text/plain") && msg.getBodyText().isEmpty() )
        {
            msg.setBodyText((String)part.getContent());

        } else if( part.isMimeType("multipart/*")) {

           Multipart multipart = (Multipart) part.getContent();

           for( int i = 0; i < multipart.getCount(); i++ )
           {
              BodyPart sub_part = multipart.getBodyPart(i);

              parse(msg,sub_part);
           }
        } else {
            String disp = part.getDisposition();

            if( disp == null && part.getFileName() == null && part.isMimeType("text/*") ) {
                byte[] bytes = getContent(part);

                String body = new String(bytes, getCharset(part.getContentType()));

                if ( part.isMimeType("text/html") ) {
                    msg.setBodyHtml(body);
                    LOGGER.debug(msg.getBodyHtml());
                } else if ( part.isMimeType("text/rtf") ) {
                    msg.setBodyRTF(body);
                    LOGGER.debug(msg.getBodyRTF());
                } else {
                    msg.setBodyText(body);
                    LOGGER.debug(msg.getBodyText());
                }
            } else if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE)) {
                // many mailers don't include a Content-Disposition

                MimeBodyPart mpart = (MimeBodyPart)part;

                FileAttachment att = new FileAttachment();
                att.setMimeTag(getMime(part.getContentType()));
                att.setFilename(part.getFileName());
                att.setExtension(part.getFileName().substring(part.getFileName().lastIndexOf('.') + 1));

                String cid = mpart.getContentID();
                if( cid != null ) {
                    cid = StringUtils.strip(cid, "<>");
                    att.setContentId(cid);
                }

                if( att.getFilename() == null ) {
                    att.setFilename("");
                }

                att.setData(getContent(part));

                msg.addAttachment(att);
            } else {
                LOGGER.warn("Unparseable part");
            }
        }
    }

    private String getCharset( String content )
    {
        if( content.matches(".*;\\s*charset=.*") )
        {
            int idx = content.indexOf('=');

            String charset = content.substring(idx+1);

            byte[] c = new byte[2];
            c[0] = ' ';
            c[1] = '\0';

            charset = StringUtils.strip(charset,"\"");

            try {
                new String(c,charset);
            } catch( UnsupportedEncodingException ex ) {
                LOGGER.error("Invalid encoding: " + content + "=>'" + charset +"'", ex);
                return "ASCII";
            }

            return charset;
        }

        return "ASCII";
    }

    private byte[] getContent(Part mp) throws IOException, MessagingException
    {
        InputStream in = mp.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        for (int len; (len = in.read(bytes)) > 0;) {
            bos.write(bytes, 0, len);
        }

        return bos.toByteArray();
    }

    private String getMime( String content_type )
    {
        int idx = content_type.indexOf('\n');
        if( idx < 0 ) {
            return content_type;
        }

        String mime =  content_type.substring(0, idx).trim();

        return StringUtils.strip_post(mime,";");
    }

    private String getFirstHeader(String[] headers)
    {
        if( headers == null ) {
            return "";
        }

        return headers[0];
    }

    private static String getAddresses(Address[] addresses)
    {
        if( addresses == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for( Address addr : addresses ) {
            if( sb.length() > 0 ) {
                sb.append(",");
            }

            sb.append(addr.toString());
        }

        return sb.toString();
    }

    private String getHeaders(Enumeration<Header> allHeaders)
    {
       StringBuilder sb = new StringBuilder();

       while( allHeaders.hasMoreElements() )
       {
           Header h = allHeaders.nextElement();
           sb.append(h.getName());
           sb.append(": ");
           sb.append(h.getValue());

           if( allHeaders.hasMoreElements() ) {
               sb.append("\r\n");
           }
       }

       return sb.toString();
    }

}
