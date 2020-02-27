package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.PidTag;

import static com.auxilii.msgparser.Ptyp.PtypBinary;

public class PropPtypByteArray extends PropType {

    private final int length;

    public PropPtypByteArray(PidTag tag, int length) {
        super(tag, PtypBinary);
        this.length = length + 4;
    }

    @Override
    protected long getPropertiesContent() {
        return length;
    }
}
