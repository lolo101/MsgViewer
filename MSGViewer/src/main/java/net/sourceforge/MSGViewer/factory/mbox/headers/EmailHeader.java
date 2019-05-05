package net.sourceforge.MSGViewer.factory.mbox.headers;

import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.RecipientType;
import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class EmailHeader extends HeaderParser
{
    private static final Logger LOGGER = Logger.getLogger(EmailHeader.class);
    private final RecipientType type;

    EmailHeader(RecipientType type)
    {
        super( type.toString() );
        this.type = type;
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

    public void assign(Message msg, List<MailAddress> emails)
    {
        for (MailAddress email : emails) {
            RecipientEntry recipientEntry = new RecipientEntry();
            recipientEntry.setEmail(email.getEmail());
            recipientEntry.setName(email.getDisplayName());
            recipientEntry.setType(type);
            msg.addRecipient(recipientEntry);
        }
    }

    private static List<MailAddress> splitAttendees(String text)
    {
        String[] parts = text.split(",");

        List<MailAddress> addresses = new ArrayList<>();

        for( String part : parts )
        {
            MailAddress addr = mailAddressFrom(part);

            if( addr.getEmail().contains("@") ) {
                addresses.add(addr);
            }
        }

        return addresses.isEmpty() ? null : addresses;
    }

    private static MailAddress mailAddressFrom(String part) {
        int start = part.indexOf('<');
        int end = part.indexOf('>');

        if( start >= 0 && end >= 0)
        {
            String displayName = StringUtils.strip(part.substring(0, start).trim(), "\"");
            String email = part.substring(start + 1, end);
            return new MailAddress(displayName, email);
        }
        else
        {
            return new MailAddress(null, part);
        }
    }
}
