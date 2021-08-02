package net.sourceforge.MSGViewer.factory.mbox.headers;

import at.redeye.FrameWork.utilities.StringUtils;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.RecipientType;
import net.sourceforge.MSGViewer.factory.mbox.MailAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class EmailHeader extends HeaderParser {
    private static final Logger LOGGER = LogManager.getLogger(EmailHeader.class);
    private final RecipientType type;

    EmailHeader(RecipientType type) {
        super(type.toString());
        this.type = type;
    }

    @Override
    public void parse(Message msg, String line) {
        LOGGER.debug("line: " + line);
        splitAttendees(line).forEach(msg::addRecipient);
    }

    protected List<RecipientEntry> splitAttendees(String text) {
        return Arrays.stream(text.split(","))
                .map(EmailHeader::mailAddressFrom)
                .filter(addr -> addr.getEmail().contains("@"))
                .map(this::toRecipientEntry)
                .collect(toList());
    }

    private static MailAddress mailAddressFrom(String part) {
        int start = part.indexOf('<');
        int end = part.indexOf('>');

        if (start < 0 || end < 0) {
            return new MailAddress(null, part);
        }
        String displayName = StringUtils.strip(part.substring(0, start).trim(), "\"");
        String email = part.substring(start + 1, end);
        return new MailAddress(displayName, email);
    }

    private RecipientEntry toRecipientEntry(MailAddress email) {
        RecipientEntry recipientEntry = new RecipientEntry();
        recipientEntry.setEmail(email.getEmail());
        recipientEntry.setName(email.getDisplayName());
        recipientEntry.setType(type);
        return recipientEntry;
    }
}
