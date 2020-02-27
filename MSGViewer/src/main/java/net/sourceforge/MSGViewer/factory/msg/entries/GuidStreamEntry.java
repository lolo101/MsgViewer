package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.PidTag.PidTagGuidStream;

public class GuidStreamEntry extends BinaryEntry {

    public GuidStreamEntry(byte[] guid) {
        super(PidTagGuidStream, guid);
    }
}
