package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientType;

public class FromHeader extends RecipientHeader {
    public FromHeader() {
        super(RecipientType.FROM);
    }

    @Override
    public void parse(Message msg, String line) {
        splitAttendees(line).forEach(entry -> {
            msg.setFromEmail(entry.getEmail());
            msg.setFromSMTPAddress(entry.getEmail());
            msg.setFromName(entry.getName());
            msg.setFromAddressType("SMTP");
        });
    }
}
