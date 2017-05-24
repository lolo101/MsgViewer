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
            recipientEntry.setToEmail(email.getEmail());
            recipientEntry.setToName(email.getDisplayName());
            recipientEntry.setProperty("type", "To");
            msg.addRecipient(recipientEntry);
        }
    }
}
