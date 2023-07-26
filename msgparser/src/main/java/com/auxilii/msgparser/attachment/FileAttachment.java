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

import com.auxilii.msgparser.Property;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * Implementation of the {@link Attachment} interface that represents a file attachment. It contains some useful
 * information (as long as it is available in the .msg file) like the attachment name, its size, etc.
 *
 * @author roman.kurmanowytsch
 */
public class FileAttachment implements Attachment {

    private String displayName;

    /**
     * The (by Outlook) shortened filename of the attachment.
     */
    private String filename;
    /**
     * The full filename of the attachment.
     */
    private String longFilename;
    /**
     * Mime type of the attachment
     */
    private String mimeTag;
    /**
     * The extension of the attachment (may not be set).
     */
    private String extension;
    /**
     * The attachment itself as a byte array.
     */
    private byte[] data;

    /**
     * AttachContentId
     */
    private String contentId;
    private String contentLocation;

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the extension
     * @see <a href="https://learn.microsoft.com/en-us/office/client-developer/outlook/mapi/pidtagattachextension-canonical-property">PidTagAttachExtension Canonical Property</a>
     */
    public String getExtension() {
        if (extension != null)
            return extension;
        String extensionFromFilename = extensionFromFilename();
        return extensionFromFilename.isEmpty()
                ? extensionFromFilename
                : "." + extensionFromFilename;
    }

    private String extensionFromFilename() {
        if (longFilename != null)
            return FilenameUtils.getExtension(longFilename);
        if (filename != null)
            return FilenameUtils.getExtension(filename);
        return "";
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
     * @return the long filename if present, 8.3 filename otherwise
     */
    public String getLongFilename() {
        return longFilename == null ? filename : longFilename;
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

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public void setProperty(Property property) throws ClassCastException {
        switch (property.getPid()) {
            case PidTagDisplayName:
                setDisplayName((String) property.getValue());
                break;
            case PidTagAttachDataBinary:
                setData((byte[]) property.getValue());
                break;
            case PidTagAttachExtension:
                setExtension((String) property.getValue());
                break;
            case PidTagAttachFilename:
                setFilename((String) property.getValue());
                break;
            case PidTagAttachLongFilename:
                setLongFilename((String) property.getValue());
                break;
            case PidTagAttachMimeTag:
                setMimeTag((String) property.getValue());
                break;
            case PidTagAttachContentId:
                setContentId((String) property.getValue());
                break;
            case PidTagAttachContentLocation:
                setContentLocation((String) property.getValue());
                break;
        }
    }

    /**
     * Returns either the display name, the long filename or the short filename, the first non-blank in that order.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Stream.of(displayName, longFilename, filename)
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse(null);
    }
}
