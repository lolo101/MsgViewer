package net.sourceforge.MSGViewer.factory.mbox.headers;

import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.RecipientType;
import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class RecipientHeader {
    private static final Logger LOGGER = LogManager.getLogger(RecipientHeader.class);
    private final RecipientType type;

    RecipientHeader(RecipientType type) {
        this.type = type;
    }

    public void parse(Message msg, String line) {
        LOGGER.debug("line: " + line);
        splitAttendees(line).forEach(msg::addRecipient);
    }

    protected Stream<RecipientEntry> splitAttendees(String text) {
        return Arrays.stream(text.split(","))
                .map(RecipientHeader::mailAddressFrom)
                .filter(addr -> addr.email().contains("@"))
                .map(this::toRecipientEntry);
    }

    private static MailAddress mailAddressFrom(String part) {
        int start = part.indexOf('<');
        int end = part.indexOf('>');

        if (start < 0 || end < 0) {
            return new MailAddress(part, part);
        }
        String displayName = StringUtils.strip(part.substring(0, start).trim(), "\"");
        String email = part.substring(start + 1, end);
        return new MailAddress(displayName, email);
    }

    private RecipientEntry toRecipientEntry(MailAddress email) {
        RecipientEntry recipientEntry = new RecipientEntry();
        recipientEntry.setEmail(email.email());
        recipientEntry.setName(email.displayName());
        recipientEntry.setType(type);
        return recipientEntry;
    }
}
