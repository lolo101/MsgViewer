package com.auxilii.msgparser.entries;

import com.auxilii.msgparser.Pid;
import com.auxilii.msgparser.properties.PropPtypByteArray;
import com.auxilii.msgparser.properties.PropType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.auxilii.msgparser.Ptyp.PtypBinary;

public class BinaryEntry extends SubStorageEntry {

    private final byte[] value;

    public BinaryEntry(Pid tag, byte[] value) {
        super(tag, PtypBinary);
        this.value = value;
    }

    @Override
    public PropType getPropType() {
        return new PropPtypByteArray(getTag(), value.length);
    }

    @Override
    protected InputStream createEntryContent() {
        return new ByteArrayInputStream(value);
    }

}
