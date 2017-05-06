package net.sourceforge.MSGViewer.factory.msg.entries;

public class GuidStreamEntry extends BinaryEntry {

    private static final String NAME = "0002";

    public GuidStreamEntry(byte[] guid) {
        super(NAME, guid);
    }
}
