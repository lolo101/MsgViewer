package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.PidTag;

import static com.auxilii.msgparser.Ptyp.PtypString;

public class PropPtypString extends PropType {

    private final int length;

    public PropPtypString(PidTag tag, int length) {
        super(tag, PtypString);
        this.length = length * 2 + 2;
    }

    @Override
    protected long getPropertiesContent() {
        return length;
    }
}
