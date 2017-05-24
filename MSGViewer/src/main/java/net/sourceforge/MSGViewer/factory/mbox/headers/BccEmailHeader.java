package net.sourceforge.MSGViewer.factory.mbox.headers;

import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
import java.util.List;

/**
 *
 * @author martin
 */
public class BccEmailHeader extends EmailHeader
{
    public BccEmailHeader()
    {
        super("Bcc");
    }

    @Override
    public void assign(Message msg, List<MailAddress> emails)
    {
        for (MailAddress email : emails) {
            msg.addBccEmail(email.getEmail());
            msg.addBccName(email.getDisplayName());
        }
    }
}
