package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagRtfCompressed;

public class CompressedRTFEntry extends BinaryEntry {
    public CompressedRTFEntry(byte[] compressedRTF) {
        super(PidTagRtfCompressed, compressedRTF);
    }
}
