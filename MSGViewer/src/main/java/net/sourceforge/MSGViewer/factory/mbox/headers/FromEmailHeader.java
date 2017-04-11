/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.mbox.headers;

import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import com.auxilii.msgparser.Message;
import java.util.List;

/**
 *
 * @author martin
 */
public class FromEmailHeader extends EmailHeader 
{
    public FromEmailHeader()
    {
        super("From");
    }

    @Override
    public void assign(Message msg, List<MailAddress> emails) 
    {       
        msg.setFromEmail(emails.get(0).getEmail());
        msg.setFromName(emails.get(0).getDisplayName());
    }
}
