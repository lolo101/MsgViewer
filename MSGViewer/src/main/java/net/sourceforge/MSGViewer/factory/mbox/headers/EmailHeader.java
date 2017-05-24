package net.sourceforge.MSGViewer.factory.mbox.headers;

import at.redeye.FrameWork.utilities.StringUtils;
import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public abstract class EmailHeader extends HeaderParser
{
    private static final Logger LOGGER = Logger.getLogger(EmailHeader.class);

    public EmailHeader( String header )
    {
        super( header );
    }

    @Override
    public void parse(Message msg, String line)
    {
       LOGGER.debug("line: " + line);

       List<MailAddress> emails = splitAttendees( line );

       if( emails != null && !emails.isEmpty() ) {
           assign( msg, emails );
       }
    }

    public abstract void assign( Message msg, List<MailAddress> emails );

    public static List<MailAddress> splitAttendees(String text)
    {
        String parts[] = text.split(",");

        List<MailAddress> addresses = new ArrayList<>();

        for( String part : parts )
        {
            MailAddress addr = new MailAddress();

            int start = part.indexOf('<');
            int end = part.indexOf('>');

            if( start >= 0 && end >= 0)
            {
                addr.setEmail(part.substring(start+1,end));
                addr.setDisplayName(StringUtils.strip(part.substring(0,start).trim(), "\""));
            }
            else
            {
                addr.setEmail(part);
            }

            if( addr.getEmail().contains("@") ) {
                addresses.add(addr);
            }
        }

        return addresses.isEmpty() ? null : addresses;
    }

}
