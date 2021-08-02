package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.Ptyp;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import net.sourceforge.MSGViewer.factory.msg.entries.*;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypInteger32;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypTime;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.stream.Collectors;

import static com.auxilii.msgparser.Pid.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class MsgContainer {
    public static final String PROPERTY_STREAM = "__properties_version1.0";
    public static final String NAMED_PROPERTY = "__nameid_version1.0";

    private final List<PropType> properties = new ArrayList<>();
    private final List<SubstGEntry> substg_streams = new ArrayList<>();

    private final List<RecipientEntry> recipients = new ArrayList<>();
    private final List<Attachment> attachments = new ArrayList<>();

    MsgContainer(Message msg) {
        if (msg.getSubject() != null) {
            addVarEntry(new SubjectEntry(msg.getSubject()));
        }

        addVarEntry(new MessageClassEntry());

        if (isNotEmpty(msg.getBodyText())) {
            addVarEntry(new BodyTextEntry(msg.getBodyText()));
        }

        if (msg.getCompressedRTF() != null) {
            addVarEntry(new CompressedRTFEntry(msg.getCompressedRTF()));
        }

        if (msg.getHeaders() != null && !msg.getHeaders().isEmpty()) {
            addVarEntry(new HeadersEntry(msg.getHeaders()));
        }

        // PidTagStoreSupportMask data is encoded in unicode
        addProperty(new PropPtypInteger32(PidTagStoreSupportMask, 0x00040000));

        // PidTagCreationTime
        addProperty(new PropPtypTime(PidTagCreationTime, System.currentTimeMillis()));

        // PidTagLastModificationTime
        addProperty(new PropPtypTime(PidTagLastModificationTime, System.currentTimeMillis()));

        // PidTagClientSubmitTime
        if (msg.getDate() != null) {
            addProperty(new PropPtypTime(PidTagClientSubmitTime, msg.getDate().getTime()));
        }

        if (isNotBlank(msg.getMessageId())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagInternetMessageId, msg.getMessageId()));
        }

        if (isNotBlank(msg.getFromName())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagSenderName, msg.getFromName()));
            addVarEntry(new StringUTF16SubstgEntry(PidTagSentRepresentingName, msg.getFromName()));
        }

        if (isNotBlank(msg.getFromEmail())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagSenderEmailAddress, msg.getFromEmail()));
            addVarEntry(new StringUTF16SubstgEntry(PidTagSentRepresentingEmailAddress, msg.getFromEmail()));
        }

        if (isNotBlank(msg.getToName())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagDisplayName, msg.getToName()));
        }

        if (isNotBlank(msg.getToEmail())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagEmailAddress, msg.getToEmail()));
        }

        if (isNotBlank(msg.getDisplayTo())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagDisplayTo, msg.getDisplayTo()));
        }

        if (isNotBlank(msg.getDisplayCc())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagDisplayCc, msg.getDisplayCc()));
        }

        if (isNotBlank(msg.getDisplayBcc())) {
            addVarEntry(new StringUTF16SubstgEntry(PidTagDisplayBcc, msg.getDisplayBcc()));
        }

        for (RecipientEntry rec_entry : msg.getRecipients()) {
            addRecipient(rec_entry);
        }

        for (Attachment attachment : msg.getAttachments()) {
            addAttachment(attachment);
        }
    }

    public void write(DirectoryEntry root) throws IOException {
        int headerSize = root.getParent() == null ? 32 : 24;
        int size = headerSize + properties.size() * 16;
        ByteBuffer bytes = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);

        bytes.position(8);

        // next recip id
        bytes.putInt(recipients.size());

        // next attachment id
        bytes.putInt(attachments.size());

        // recip count
        bytes.putInt(recipients.size());

        // attachment count
        bytes.putInt(attachments.size());

        bytes.position(headerSize);

        for (PropType prop : properties) {
            prop.writePropertiesEntry(bytes);
        }

        for (SubstGEntry entry : substg_streams) {
            entry.createEntry(root);
        }

        for (int count = 0; count < recipients.size(); ++count) {
            writeRecipientEntry(root, recipients.get(count), count);
        }

        for (int count = 0; count < attachments.size(); ++count) {
            writeAttachment(root, attachments.get(count), count);
        }

        createPropertyStreamEntry(bytes, root);
        createNamedPropertyEntry(root);
    }

    private void addProperty(PropType prop) {
        properties.add(prop);
    }

    private void addVarEntry(SubstGEntry entry) {
        addProperty(entry.getPropType());
        substg_streams.add(entry);
    }

    private void addRecipient(RecipientEntry entry) {
        recipients.add(entry);
    }

    private void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    private static void createPropertyStreamEntry(ByteBuffer bytes, DirectoryEntry root) throws IOException {
        deleteEntryIfExists(root, PROPERTY_STREAM);

        InputStream buffer = new ByteArrayInputStream(bytes.array());
        root.createDocument(PROPERTY_STREAM, buffer);
    }

    private static void createNamedPropertyEntry(DirectoryEntry root) throws IOException {
        deleteEntryIfExists(root, NAMED_PROPERTY);

        DirectoryEntry prop_entry = root.createDirectory(NAMED_PROPERTY);

        new GuidStreamEntry(new byte[0]).createEntry(prop_entry);
        new EntryStreamEntry(new byte[0]).createEntry(prop_entry);
        new StringStreamEntry(new byte[0]).createEntry(prop_entry);
    }

    private static void deleteEntryIfExists(DirectoryEntry root, String name) {
        try {
            if (root.hasEntry(name)) {
                root.getEntry(name).delete();
            }
        } catch (FileNotFoundException ignore) {
        }
    }

    private static void writeRecipientEntry(DirectoryEntry root, RecipientEntry rec, int id) throws IOException {
        DirectoryEntry rec_dir = root.createDirectory(String.format("__recip_version1.0_#%08X", id));

        List<? extends SubstGEntry> entries = Arrays.asList(
                new StringUTF16SubstgEntry(PidTagDisplayName, rec.getName()),
                new StringUTF16SubstgEntry(PidTagAddressType, "SMTP"),
                new StringUTF16SubstgEntry(PidTagEmailAddress, rec.getEmail())
        );

        for (SubstGEntry entry : entries) {
            entry.createEntry(rec_dir);
        }

        List<PropType> props = new ArrayList<>();
        props.add(new PropPtypInteger32(PidTagRowid, id));
        props.add(new PropPtypInteger32(PidTagRecipientType, rec.getType().getValue()));
        for (SubstGEntry entry : entries) {
            props.add(entry.getPropType());
        }

        ByteBuffer bytes = createPropertiesEntryContent(props);

        createPropertyStreamEntry(bytes, rec_dir);
    }

    private static void writeAttachment(DirectoryEntry root, Attachment attachment, int id) throws IOException {
        DirectoryEntry att_dir = root.createDirectory(String.format("__attach_version1.0_#%08X", id));
        if (attachment instanceof FileAttachment) {
            writeFileAttachment((FileAttachment) attachment, att_dir);
        }
        if (attachment instanceof MsgAttachment) {
            writeMsgAttachment((MsgAttachment) attachment, att_dir);
        }
    }

    private static void writeFileAttachment(FileAttachment attachment, DirectoryEntry att_dir) throws IOException {
        List<? extends SubstGEntry> entries = Arrays.asList(
                new BinaryEntry(PidTagAttachDataBinary, attachment.getData()),
                new StringUTF16SubstgEntry(PidTagAttachExtension, attachment.getExtension()),
                new StringUTF16SubstgEntry(PidTagAttachFilename, attachment.getFilename()),
                new StringUTF16SubstgEntry(PidTagAttachLongFilename, attachment.getLongFilename()),
                new StringUTF16SubstgEntry(PidTagAttachMimeTag, attachment.getMimeTag())
        );

        for (SubstGEntry entry : entries) {
            entry.createEntry(att_dir);
        }

        List<PropType> props = entries.stream()
                .map(SubstGEntry::getPropType)
                .collect(Collectors.toList());

        ByteBuffer bytes = createPropertiesEntryContent(props);

        createPropertyStreamEntry(bytes, att_dir);
    }

    private static void writeMsgAttachment(MsgAttachment attachment, DirectoryEntry att_dir) throws IOException {
        ByteBuffer bytes = createPropertiesEntryContent(Collections.emptyList());

        createPropertyStreamEntry(bytes, att_dir);

        DirectoryEntry msg_dir = att_dir.createDirectory(Ptyp.SUBSTORAGE_PREFIX + "3701000D");
        writeMessage(attachment.getMessage(), msg_dir);
    }

    private static void writeMessage(Message message, DirectoryEntry msg_dir) throws IOException {
        MsgContainer container = new MsgContainer(message);
        container.write(msg_dir);
    }

    private static ByteBuffer createPropertiesEntryContent(Collection<PropType> p_entries) {
        int size = 8 + p_entries.size() * 16;
        ByteBuffer bytes = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
        bytes.position(8);

        for (PropType prop : p_entries) {
            prop.writePropertiesEntry(bytes);
        }

        return bytes;
    }
}
