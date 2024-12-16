package com.auxilii.msgparser.entries;

import static com.auxilii.msgparser.Pid.PidTagMessageClass;

public class MessageClassEntry extends StringUTF16SubstgEntry {
    private static final String IPM_NOTE = "IPM.Note";

    public MessageClassEntry() {
        super(PidTagMessageClass, IPM_NOTE);
    }

}
