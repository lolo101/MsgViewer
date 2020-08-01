package net.sourceforge.MSGViewer.factory.msg.properties;

import com.auxilii.msgparser.Pid;

import static com.auxilii.msgparser.Ptyp.PtypBinary;

public class PropPtypByteArray extends PropType {

    private final int length;

    public PropPtypByteArray(Pid id, int length) {
        super(id, PtypBinary);
        this.length = length + 4;
    }

    @Override
    protected long getPropertiesContent() {
        return length;
    }
}
