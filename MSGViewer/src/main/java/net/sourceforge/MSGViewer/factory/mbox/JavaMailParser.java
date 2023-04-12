package net.sourceforge.MSGViewer.factory.mbox;

import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import jakarta.mail.*;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimePart;
import jakarta.mail.internet.MimeUtility;
import net.sourceforge.MSGViewer.factory.FileExtension;
import net.sourceforge.MSGViewer.factory.mbox.headers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class JavaMailParser {
    private static final Logger LOGGER = LogManager.getLogger(JavaMailParser.class);
    private static final RecipientHeader FROM_PARSER = new FromHeader();
    private static final RecipientHeader TO_PARSER = new ToHeader();
    private static final RecipientHeader CC_PARSER = new CcHeader();
    private static final RecipientHeader BCC_PARSER = new BccHeader();
    private static final DateHeader DATE_PARSER = new DateHeader();
    private static final Pattern CHARSET_PATTERN = Pattern.compile(".*;\\s*charset=.*");
    private final Path file;

    public JavaMailParser(Path file) {
        this.file = file;
    }

    public Message parse() throws Exception {
        jakarta.mail.Message jmsg = parseJMessage();

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

    private jakarta.mail.Message parseJMessage() throws MessagingException, IOException {
        try (InputStream stream = Files.newInputStream(file)) {
            Session session = Session.getInstance(System.getProperties());
            return new MimeMessage(session, stream);
        }
    }

    private static void parse(Message msg, Part part) throws MessagingException, IOException
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
                LOGGER.debug(body);

                if ( part.isMimeType("text/html") ) {
                    msg.setBodyHtml(bytes);
                } else if ( part.isMimeType("text/rtf") ) {
                    msg.setBodyRTF(body);
                } else {
                    msg.setBodyText(body);
                }
            } else if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE)) {
                // many mailers don't include a Content-Disposition

                MimePart mpart = (MimePart) part;

                FileAttachment att = new FileAttachment();
                String filename = MimeUtility.decodeText(part.getFileName());
                att.setMimeTag(getMime(part.getContentType()));
                att.setFilename(filename);
                att.setExtension(new FileExtension(filename).toString());

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

    private static String getCharset(String content)
    {
        if (CHARSET_PATTERN.matcher(content).matches()) {
            int idx = content.indexOf('=');

            String charset = content.substring(idx + 1);

            byte[] c = new byte[2];
            c[0] = ' ';

            charset = StringUtils.strip(charset, "\"");

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

    private static byte[] getContent(Part mp) throws IOException, MessagingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (InputStream in = mp.getInputStream()) {
            in.transferTo(bos);
        }
        return bos.toByteArray();
    }

    private static String getMime(String content_type)
    {
        int idx = content_type.indexOf('\n');
        if( idx < 0 ) {
            return content_type;
        }

        String mime =  content_type.substring(0, idx).trim();

        return StringUtils.strip_post(mime,";");
    }

    private static String getFirstHeader(String[] headers)
    {
        if( headers == null ) {
            return "";
        }

        return headers[0];
    }

    private static String getAddresses(Address[] addresses) {
        if (addresses == null) {
            return "";
        }

        return Arrays.stream(addresses)
                .map(Address::toString)
                .collect(joining(","));
    }

    private static String getHeaders(Enumeration<Header> allHeaders)
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
