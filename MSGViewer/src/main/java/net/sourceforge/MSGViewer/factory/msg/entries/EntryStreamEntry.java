package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.PidTag.PidTagEntryStream;

public class EntryStreamEntry extends BinaryEntry {

    public EntryStreamEntry(byte[] entry) {
        super(PidTagEntryStream, entry);
    }
}
