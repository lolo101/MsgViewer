/*
 * msgparser - http://auxilii.com/msgparser
 * Copyright (C) 2007  Roman Kurmanowytsch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.auxilii.msgparser;

import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.IOException;

public class FieldInformation {
    private final DocumentEntry entry;

    public FieldInformation(DocumentEntry de) {
        this.entry = de;
    }

    public Pid getId() {
        int index = Ptyp.SUBSTORAGE_PREFIX.length();
        return Pid.from(Integer.parseInt(entry.getName().substring(index, index + 4), 16));
    }

    public Object getData() throws IOException {
        Ptyp type = getType();
        try (DocumentInputStream dstream = new DocumentInputStream(entry)) {
            return type.convert(dstream);
        }
    }

    private Ptyp getType() {
        int index = Ptyp.SUBSTORAGE_PREFIX.length();
        Ptyp type = Ptyp.from(Integer.parseInt(entry.getName().substring(index + 4, index + 8), 16));
        return isOneOfMultipleVariableLengthEntry() ? Ptyp.from(type.id ^ Ptyp.MULTIPLE_VALUED_FLAG) : type;
    }

    private boolean isOneOfMultipleVariableLengthEntry() {
        return entry.getName().contains("-");
    }
}
