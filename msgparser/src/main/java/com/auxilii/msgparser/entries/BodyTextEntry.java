package com.auxilii.msgparser.entries;

import static com.auxilii.msgparser.Pid.PidTagBody;

public class BodyTextEntry extends StringUTF16SubstgEntry {
    public BodyTextEntry(String text) {
        super(PidTagBody, text);
    }

}
