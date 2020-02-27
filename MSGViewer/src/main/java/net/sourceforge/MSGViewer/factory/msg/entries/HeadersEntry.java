package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagTransportMessageHeaders;

public class HeadersEntry extends StringUTF16SubstgEntry {
    public HeadersEntry(String headers) {
        super(PidTagTransportMessageHeaders, headers);
    }

}
