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
package com.auxilii.msgparser.attachment;

import org.apache.poi.poifs.filesystem.DocumentEntry;

/**
 * Implementation of the {@link Attachment} interface that represents a file attachment. It contains some useful
 * information (as long as it is available in the .msg file) like the attachment name, its size, etc.
 *
 * @author roman.kurmanowytsch
 */
public class FileAttachment implements Attachment {

    /**
     * The (by Outlook) shortened filename of the attachment.
     */
    private String filename = null;
    /**
     * The full filename of the attachment.
     */
    private String longFilename = null;
    /**
     * Mime type of the attachment
     */
    private String mimeTag = null;
    /**
     * The extension of the attachment (may not be set).
     */
    private String extension = null;
    /**
     * The attachment itself as a byte array.
     */
    private byte[] data = null;
    /**
     * The size of the attachment.
     */
    private long size = -1;

    /**
     * Subdir where the attachment was found
     */
    private String subdir = null;

    /**
     * AttachContentId
     */
    private String cid = null;

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the longFilename
     */
    public String getLongFilename() {
        return longFilename;
    }

    /**
     * @param longFilename the longFilename to set
     */
    public void setLongFilename(String longFilename) {
        this.longFilename = longFilename;
    }

    /**
     * @return the mimeTag
     */
    public String getMimeTag() {
        return mimeTag;
    }

    /**
     * @param mimeTag the mimeTag to set
     */
    public void setMimeTag(String mimeTag) {
        this.mimeTag = mimeTag;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @return the subdir where the attachment was found
     */
    public String getSubDir() {
        return subdir;
    }

    public void setSubDir(String subdir) {
        this.subdir = subdir;
    }

    /**
     * Sets the property specified by the name parameter. Unknown names are ignored.
     *
     * @param name The name of the property (i.e., the class of the {@link DocumentEntry}).
     * @param value The value of the field.
     * @param de The current {@link DocumentEntry}.
     */
    public void setProperty(String name, Object value, DocumentEntry de) throws ClassCastException {

        if (name == null || value == null) {
            return;
        }

        switch (name) {
            case "3701":
                this.setSize(de.getSize());
                this.setData((byte[]) value);
                break;
            case "3704":
                this.setFilename((String) value);
                break;
            case "3707":
                this.setLongFilename((String) value);
                break;
            case "370e":
                this.setMimeTag((String) value);
                break;
            case "3703":
                this.setExtension((String) value);
                break;
            case "3712":
                this.setCid((String) value);
                break;
        }
    }

    /**
     * Returns either the long filename or the short filename, depending on which is available.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return longFilename == null ? filename : longFilename;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

}
