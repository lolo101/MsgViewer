package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagHtml;

public class BodyHtmlEntry extends BinaryEntry {
    public BodyHtmlEntry(byte[] html) {
        super(PidTagHtml, html);
    }

}
