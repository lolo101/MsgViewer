package net.sourceforge.MSGViewer.factory.mbox.headers;

import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import java.util.List;

/**
 *
 * @author martin
 */
public class CcEmailHeader extends EmailHeader
{
    public CcEmailHeader()
    {
        super("Cc");
    }

    @Override
    public void assign(Message msg, List<MailAddress> emails)
    {
        for (MailAddress email : emails) {
            RecipientEntry recipientEntry = new RecipientEntry();
            recipientEntry.setEmail(email.getEmail());
            recipientEntry.setName(email.getDisplayName());
            recipientEntry.setType(RecipientEntry.RecipientType.CC);
            msg.addRecipient(recipientEntry);
        }
    }
}
