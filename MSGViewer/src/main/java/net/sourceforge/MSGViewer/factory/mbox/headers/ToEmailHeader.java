package net.sourceforge.MSGViewer.factory.mbox.headers;

import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import java.util.List;

/**
 *
 * @author martin
 */
public class ToEmailHeader extends EmailHeader
{
    public ToEmailHeader()
    {
        super("To");
    }

    @Override
    public void assign(Message msg, List<MailAddress> emails)
    {
        for (MailAddress email : emails) {
            RecipientEntry recipientEntry = new RecipientEntry();
            recipientEntry.setEmail(email.getEmail());
            recipientEntry.setName(email.getDisplayName());
            recipientEntry.setType(RecipientEntry.RecipientType.TO);
            msg.addRecipient(recipientEntry);
        }
    }
}
