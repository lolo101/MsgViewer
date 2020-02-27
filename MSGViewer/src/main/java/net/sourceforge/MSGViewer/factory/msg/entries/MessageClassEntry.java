package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.PidTag.PidTagMessageClass;

public class MessageClassEntry extends StringUTF16SubstgEntry {
    public static final String IPM_NOTE = "IPM.Note";

    public MessageClassEntry() {
        super(PidTagMessageClass, IPM_NOTE);
    }

}
