package net.sourceforge.MSGViewer.factory.mbox.headers;

import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientType;
import java.util.List;

public class FromEmailHeader extends EmailHeader
{
    public FromEmailHeader()
    {
        super(RecipientType.FROM);
    }

    @Override
    public void assign(Message msg, List<MailAddress> emails)
    {
        msg.setFromEmail(emails.get(0).getEmail());
        msg.setFromName(emails.get(0).getDisplayName());
    }
}
