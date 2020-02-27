package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.PidTag.PidTagStringStream;

public class StringStreamEntry extends BinaryEntry {

    public StringStreamEntry(byte[] string) {
        super(PidTagStringStream, string);
    }
}
