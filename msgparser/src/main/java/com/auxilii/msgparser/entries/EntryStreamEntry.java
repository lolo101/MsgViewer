package com.auxilii.msgparser.entries;

import static com.auxilii.msgparser.Pid.PidTagEntryStream;

public class EntryStreamEntry extends BinaryEntry {

    public EntryStreamEntry(byte[] entry) {
        super(PidTagEntryStream, entry);
    }
}
