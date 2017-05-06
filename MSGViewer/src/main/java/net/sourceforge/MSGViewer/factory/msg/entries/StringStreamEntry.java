package net.sourceforge.MSGViewer.factory.msg.entries;

public class StringStreamEntry extends BinaryEntry {

    private static final String NAME = "0004";

    public StringStreamEntry(byte[] string) {
        super(NAME, string);
    }
}
