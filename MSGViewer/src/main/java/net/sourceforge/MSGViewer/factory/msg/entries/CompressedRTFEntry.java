package net.sourceforge.MSGViewer.factory.msg.entries;

import java.io.UnsupportedEncodingException;

public class CompressedRTFEntry extends BinaryEntry
{
    public static final String NAME = "1009";

    public CompressedRTFEntry(byte[] compressedRTF) {
        super(NAME, compressedRTF);
    }
}
