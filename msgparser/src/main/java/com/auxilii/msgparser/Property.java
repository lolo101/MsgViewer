package com.auxilii.msgparser;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.IOException;

public class Property {

    private final PidTag tag;
    private final Object value;

    Property(DocumentInputStream propertyStream, DirectoryEntry dir) throws IOException {
        short pType = propertyStream.readShort();
        short id = propertyStream.readShort();
        int flags = propertyStream.readInt();
        Ptyp typ = Ptyp.from(pType);

        tag = PidTag.from(id);
        value = typ.parseValue(propertyStream, dir, String.format("%04X%04X", id, pType));
    }

    public PidTag getPid() {
        return tag;
    }

    public Object getValue() {
        return value;
    }
}
