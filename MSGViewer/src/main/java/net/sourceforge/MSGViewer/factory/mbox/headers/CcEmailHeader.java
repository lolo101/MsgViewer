package net.sourceforge.MSGViewer.factory.mbox.headers;

import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
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
            msg.addCcEmail(email.getEmail());
            msg.addCcName(email.getDisplayName());
        }
    }
}
