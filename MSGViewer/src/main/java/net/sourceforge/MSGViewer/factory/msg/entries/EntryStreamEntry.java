package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagEntryStream;

public class EntryStreamEntry extends BinaryEntry {

    public EntryStreamEntry(byte[] entry) {
        super(PidTagEntryStream, entry);
    }
}
