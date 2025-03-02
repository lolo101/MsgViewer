package com.auxilii.msgparser;

import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MsgParser {

    private static final String PROPERTIES_ENTRY = "__properties_version1.0";
    private final Path msgFile;

    public MsgParser(Path msgFile) {
        this.msgFile = msgFile;
    }

    /**
     * Parses a .msg file provided in the specified file.
     *
     * @return A {@link Message} object representing the .msg file.
     * @throws IOException Thrown if the file could not be loaded or parsed.
     */
    public Message parseMsg() throws IOException {
        try (InputStream stream = Files.newInputStream(msgFile)) {
            return parseMsg(stream);
        }
    }

    /**
     * Parses a .msg file provided by an input stream.
     *
     * @param msgFileStream The .msg file as an InputStream.
     * @return A {@link Message} object representing the .msg file.
     * @throws IOException Thrown if the file could not be loaded or parsed.
     */
    private static Message parseMsg(InputStream msgFileStream) throws IOException {
        try (POIFSFileSystem fs = new POIFSFileSystem(msgFileStream)) {
            return parseMsg(fs.getRoot());
        }
    }

    private static Message parseMsg(DirectoryEntry dir) throws IOException {
        DocumentEntry propertiesEntry = (DocumentEntry) dir.getEntry(PROPERTIES_ENTRY);
        try (DocumentInputStream propertyStream = new DocumentInputStream(propertiesEntry)) {
            propertyStream.skip(8);
            propertyStream.readInt(); // nextRecipientId
            propertyStream.readInt(); // nextAttachmentId
            int recipientCount = propertyStream.readInt();
            int attachmentCount = propertyStream.readInt();
            boolean topLevel = dir.getParent() == null;
            if (topLevel) {
                propertyStream.skip(8);
            }

            Message msg = new Message();
            for (int index = 0; index < recipientCount; index++) {
                DirectoryEntry entry = (DirectoryEntry) dir.getEntry(String.format("__recip_version1.0_#%08X", index));
                msg.addRecipient(parseRecipient(entry));
            }
            for (int index = 0; index < attachmentCount; index++) {
                DirectoryEntry entry = (DirectoryEntry) dir.getEntry(String.format("__attach_version1.0_#%08X", index));
                msg.addAttachment(parseAttachment(entry));
            }
            while (propertyStream.available() > 0) {
                msg.addProperty(new Property(propertyStream, dir));
            }
            return msg;
        }
    }

    /**
     * Parses a recipient directory entry which holds information about one of possibly multiple recipients.
     * The parsed information is put into the {@link Message} object.
     *
     * @param dir The current node in the .msg file.
     * @throws IOException Thrown if the .msg file could not
     *                     be parsed.
     */
    private static RecipientEntry parseRecipient(DirectoryEntry dir) throws IOException {
        RecipientEntry recipient = new RecipientEntry();
        DocumentEntry propertyEntry = (DocumentEntry) dir.getEntry(PROPERTIES_ENTRY);
        try (DocumentInputStream propertyStream = new DocumentInputStream(propertyEntry)) {
            propertyStream.skip(8);
            while (propertyStream.available() > 0) {
                recipient.addProperty(new Property(propertyStream, dir));
            }
        }
        return recipient;
    }

    /**
     * Creates an {@link Attachment} object based on
     * the given directory entry. The entry may either
     * point to an attached file or to an
     * attached .msg file, which will be added
     * as a {@link MsgAttachment} object instead.
     *
     * @param dir The directory entry containing the attachment
     *            document entry and some other document entries
     *            describing the attachment (name, extension, mime type, ...)
     * @throws IOException Thrown if the attachment could
     *                     not be parsed/read.
     */
    private static Attachment parseAttachment(DirectoryEntry dir) throws IOException {
        if (dir.hasEntry(Ptyp.SUBSTORAGE_PREFIX + "3701000D")) {
            return parseEmbeddedMessage(dir);
        }
        return ParseFileAttachment(dir);
    }

    private static MsgAttachment parseEmbeddedMessage(DirectoryEntry dir) throws IOException {
        DirectoryEntry entry = (DirectoryEntry) dir.getEntry(Ptyp.SUBSTORAGE_PREFIX + "3701000D");

        return new MsgAttachment(parseMsg(entry));
    }

    private static FileAttachment ParseFileAttachment(DirectoryEntry dir) throws IOException {
        DocumentEntry propertyEntry = (DocumentEntry) dir.getEntry(PROPERTIES_ENTRY);
        try (DocumentInputStream propertyStream = new DocumentInputStream(propertyEntry)) {
            propertyStream.skip(8);
            return createAttachmentWithProperties(dir, propertyStream);
        }

    }

    private static FileAttachment createAttachmentWithProperties(DirectoryEntry dir, DocumentInputStream propertyStream) throws IOException {
        FileAttachment fileAttachment = new FileAttachment();
        while (propertyStream.available() > 0) {
            Property property = new Property(propertyStream, dir);
            fileAttachment.addProperty(property);
        }
        return fileAttachment;
    }
}
