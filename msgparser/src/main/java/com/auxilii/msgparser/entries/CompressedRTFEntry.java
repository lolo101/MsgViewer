package com.auxilii.msgparser.entries;

import static com.auxilii.msgparser.Pid.PidTagRtfCompressed;

public class CompressedRTFEntry extends BinaryEntry {
    public CompressedRTFEntry(byte[] compressedRTF) {
        super(PidTagRtfCompressed, compressedRTF);
    }
}
