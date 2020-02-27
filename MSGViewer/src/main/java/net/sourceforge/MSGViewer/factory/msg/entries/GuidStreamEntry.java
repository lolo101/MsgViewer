package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagGuidStream;

public class GuidStreamEntry extends BinaryEntry {

    public GuidStreamEntry(byte[] guid) {
        super(PidTagGuidStream, guid);
    }
}
