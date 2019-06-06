package com.auxilii.msgparser;

import java.io.IOException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

class Property {

    private final short id;
    private final Ptyp typ;
    private final Object value;

    Property(DocumentInputStream propertyStream, DirectoryEntry dir) throws IOException {
        short pType = propertyStream.readShort();
        id = propertyStream.readShort();
        int flags = propertyStream.readInt();

        typ = Ptyp.from(pType);
        value = typ.parseValue(propertyStream, dir, String.format("%04X%04X", id, pType));
    }

    public String getPid() {
        return String.format("%04x", id);
    }

    public Object getValue() {
        return value;
    }
}
