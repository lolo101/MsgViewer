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

/**
 * Convenience class for storing type information
 * about a {@link DocumentEntry}.
 *
 * @author roman.kurmanowytsch
 */
public class FieldInformation {
    private final Pid id;
    private final Ptyp type;

    /**
     * Constructor that allows to set the id
     * and type of the properties.
     *
     * @param id   The id of the {@link DocumentEntry}.
     * @param type The type of the {@link DocumentEntry}.
     */
    public FieldInformation(String id, String type) {
        this.id = Pid.from(Integer.valueOf(id, 16));
        this.type = Ptyp.from(Integer.parseInt(type, 16));
    }

    public Pid getId() {
        return id;
    }

    public Ptyp getType() {
        return type;
    }
}
