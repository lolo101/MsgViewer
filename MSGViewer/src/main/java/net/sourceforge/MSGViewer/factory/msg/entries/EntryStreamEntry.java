package net.sourceforge.MSGViewer.factory.msg.entries;

public class EntryStreamEntry extends BinaryEntry {

    private static final String NAME = "0003";

    public EntryStreamEntry(byte[] entry) {
        super(NAME, entry);
    }
}
