package com.auxilii.msgparser.entries;

import static com.auxilii.msgparser.Pid.PidTagStringStream;

public class StringStreamEntry extends BinaryEntry {

    public StringStreamEntry(byte[] string) {
        super(PidTagStringStream, string);
    }
}
