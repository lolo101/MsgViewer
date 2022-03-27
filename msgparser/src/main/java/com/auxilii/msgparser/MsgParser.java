package com.auxilii.msgparser;

import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MsgParser {

    public static final String PROPERTIES_ENTRY = "__properties_version1.0";
    private final File msgFile;

    public MsgParser(File msgFile) {
        this.msgFile = msgFile;
    }

    /**
     * Parses a .msg file provided in the specified file.
     *
     * @return A {@link Message} object representing the .msg file.
     * @throws IOException Thrown if the file could not be loaded or parsed.
     */
    public Message parseMsg() throws IOException {
        try (InputStream stream = new FileInputStream(msgFile)) {
            return parseMsg(stream);
        }
    }

    /**
     * Parses a .msg file provided by an input stream.
     *
     * @param msgFileStream The .msg file as a InputStream.
     * @return A {@link Message} object representing the .msg file.
     * @throws IOException Thrown if the file could not be loaded or parsed.
     */
    private static Message parseMsg(InputStream msgFileStream) throws IOException {
        // the .msg file, like a file system, contains directories
        // and documents within this directories
        // we now gain access to the root node
        // and recursively go through the complete 'filesystem'.
        POIFSFileSystem fs = new POIFSFileSystem(msgFileStream);
        DirectoryEntry dir = fs.getRoot();
        Message msg = new Message();
        parseMsg(dir, msg);
        return msg;
    }

    private static void parseMsg(DirectoryEntry dir, Message msg) throws IOException {
        DocumentEntry propertyEntry = (DocumentEntry) dir.getEntry(PROPERTIES_ENTRY);
        try (DocumentInputStream propertyStream = new DocumentInputStream(propertyEntry)) {
            propertyStream.skip(8);
            propertyStream.readInt(); // nextRecipientId
            propertyStream.readInt(); // nextAttachmentId
            int recipientCount = propertyStream.readInt();
            int attachmentCount = propertyStream.readInt();
            boolean topLevel = dir.getParent() == null;
            if (topLevel) {
                propertyStream.skip(8);
            }

            for (int index = 0; index < recipientCount; index++) {
                DirectoryEntry entry = (DirectoryEntry) dir.getEntry(String.format("__recip_version1.0_#%08X", index));
                parseRecipient(entry, msg);
            }
            for (int index = 0; index < attachmentCount; index++) {
                DirectoryEntry entry = (DirectoryEntry) dir.getEntry(String.format("__attach_version1.0_#%08X", index));
                parseAttachment(entry, msg);
            }
            while (propertyStream.available() > 0) {
                msg.setProperty(new Property(propertyStream, dir));
            }
        }
    }

    /**
     * Parses a recipient directory entry which holds informations about one of possibly multiple recipients.
     * The parsed information is put into the {@link Message} object.
     *
     * @param dir The current node in the .msg file.
     * @param msg The resulting {@link Message} object.
     * @throws IOException Thrown if the .msg file could not
     *                     be parsed.
     */
    protected static void parseRecipient(DirectoryEntry dir, Message msg) throws IOException {
        RecipientEntry recipient = new RecipientEntry();
        DocumentEntry propertyEntry = (DocumentEntry) dir.getEntry(PROPERTIES_ENTRY);
        try (DocumentInputStream propertyStream = new DocumentInputStream(propertyEntry)) {
            propertyStream.skip(8);
            while (propertyStream.available() > 0) {
                recipient.setProperty(new Property(propertyStream, dir));
            }
        }

        msg.addRecipient(recipient);
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
     * @param msg The {@link Message} object that this
     *            attachment should be added to.
     * @throws IOException Thrown if the attachment could
     *                     not be parsed/read.
     */
    protected static void parseAttachment(DirectoryEntry dir, Message msg) throws IOException {
        if (dir.hasEntry(Ptyp.SUBSTORAGE_PREFIX + "3701000D")) {
            parseEmbeddedMessage(dir, msg);
        } else {
            ParseFileAttachment(dir, msg);
        }
    }

    private static void parseEmbeddedMessage(DirectoryEntry dir, Message msg) throws IOException {
        DirectoryEntry entry = (DirectoryEntry) dir.getEntry(Ptyp.SUBSTORAGE_PREFIX + "3701000D");
        Message attachmentMsg = new Message();
        MsgAttachment msgAttachment = new MsgAttachment();
        msgAttachment.setMessage(attachmentMsg);

        parseMsg(entry, attachmentMsg);

        msg.addAttachment(msgAttachment);
    }

    private static void ParseFileAttachment(DirectoryEntry dir, Message msg) throws IOException {
        DocumentEntry propertyEntry = (DocumentEntry) dir.getEntry(PROPERTIES_ENTRY);
        try (DocumentInputStream propertyStream = new DocumentInputStream(propertyEntry)) {
            propertyStream.skip(8);
            FileAttachment fileAttachment = createAttachmentWithProperties(dir, propertyStream);
            msg.addAttachment(fileAttachment);
        }

    }

    private static FileAttachment createAttachmentWithProperties(DirectoryEntry dir, DocumentInputStream propertyStream) throws IOException {
        FileAttachment fileAttachment = new FileAttachment();
        while (propertyStream.available() > 0) {
            Property property = new Property(propertyStream, dir);
            fileAttachment.setProperty(property);
        }
        return fileAttachment;
    }
}
