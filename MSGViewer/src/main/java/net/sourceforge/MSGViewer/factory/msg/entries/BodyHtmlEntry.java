package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagBodyHtml;

public class BodyHtmlEntry extends StringUTF16SubstgEntry {
    public BodyHtmlEntry(String text) {
        super(PidTagBodyHtml, text);
    }

}
