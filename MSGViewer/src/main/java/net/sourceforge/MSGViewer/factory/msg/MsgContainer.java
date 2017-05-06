package net.sourceforge.MSGViewer.factory.msg;

import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypInteger32;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import net.sourceforge.MSGViewer.factory.msg.entries.StringUTF16SubstgEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.SubstGEntry;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sourceforge.MSGViewer.factory.msg.entries.BinaryEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.EntryStreamEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.GuidStreamEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.StringStreamEntry;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.Entry;

/**
 *
 * @author martin
 */
public class MsgContainer
{
    public static final String NAME = "__properties_version1.0";
    public static final String NAMED_NAME = "__nameid_version1.0";

    private static final int HEADER_SIZE = 8 + 4 + 4 + 4 + 4 + 8;
    private final List<PropType> properties = new ArrayList<>();
    private final List<SubstGEntry> substg_streams = new ArrayList<>();

    private final List<RecipientEntry> recipients = new ArrayList<>();
    private final List<Attachment> attachments = new ArrayList<>();

    public void addProperty( PropType prop )
    {
        properties.add(prop);
    }

    public void addVarEntry( SubstGEntry entry )
    {
       addProperty(entry.getPropType());
       substg_streams.add(entry);
    }

    public void write( DirectoryEntry root ) throws IOException
    {
        int size = HEADER_SIZE + properties.size() * 16;
        byte bytes[] = new byte[size];

        int offset = 8;

        // next recip id
        writeInt(bytes,offset, recipients.size());
        offset +=4;

        // next attachment id
        writeInt(bytes,offset, attachments.size());
        offset +=4;

        // recip count
        writeInt(bytes,offset, recipients.size());
        offset +=4;

        // attachment count
        writeInt(bytes,offset, attachments.size());
        offset +=4;

        offset = HEADER_SIZE;

        for( PropType prop : properties )
        {
            prop.writePropertiesEntry(bytes, offset);
            offset += 16;
        }

        for( SubstGEntry entry : substg_streams ) {
            entry.createEntry(root);
        }

        for(int count = 0; count < recipients.size(); ++count ) {
            writeRecipientEntry( root, recipients.get(count), count );
        }

        for(int count = 0; count < attachments.size(); ++count ) {
            writeAttachment(root, attachments.get(count), count);
        }

        createPropEntry(bytes,root);
        createNamedEntry(root);
    }

    private void writeInt( byte[] bytes, int offset, int value ) {

       ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
       buffer.putInt(value);

       byte[] int_bytes = buffer.array();

       System.arraycopy(int_bytes, 0, bytes, offset, 4);
    }

    private void createPropEntry(byte[] bytes, DirectoryEntry root ) throws IOException
    {
        deleteEntryIfExists(root, NAME);

        ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);
        root.createDocument(NAME, buffer);
    }

    private void createNamedEntry(DirectoryEntry root ) throws IOException
    {
        deleteEntryIfExists(root, NAMED_NAME);

        DirectoryEntry prop_entry = root.createDirectory(NAMED_NAME);

        new GuidStreamEntry(new byte[0]).createEntry(prop_entry);
        new EntryStreamEntry(new byte[0]).createEntry(prop_entry);
        new StringStreamEntry(new byte[0]).createEntry(prop_entry);
    }

    private static void deleteEntryIfExists(DirectoryEntry root, String name) {
        try {
            Entry prop_entry = root.getEntry(name);
            if( prop_entry != null ) {
                prop_entry.delete();
            }
        } catch( FileNotFoundException ignore) {}
    }

    public void addRecipient( RecipientEntry entry ) {
        recipients.add(entry);
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    private void writeRecipientEntry(DirectoryEntry root , RecipientEntry rec, int id) throws IOException
    {
        DirectoryEntry rec_dir = root.createDirectory(String.format("__recip_version1.0_#%08d", id));

        List<? extends SubstGEntry> entries = Arrays.asList(
                new StringUTF16SubstgEntry("3001", rec.getToName()),
                new StringUTF16SubstgEntry("3003", rec.getToEmail())
        );

        for (SubstGEntry entry : entries) {
            entry.createEntry(rec_dir);
        }

        List<PropType> props = new ArrayList<>();
        props.add(new PropPtypInteger32("3000", id));
        props.add(new PropPtypInteger32("0C15", 1));
        for (SubstGEntry entry : entries) {
            props.add(entry.getPropType());
        }

        byte[] bytes = createPropertiesEntryContent(props);

        createPropEntry(bytes, rec_dir);
    }

    private void writeAttachment(DirectoryEntry root, Attachment attachment, int id) throws IOException {
        DirectoryEntry att_dir = root.createDirectory(String.format("__attach_version1.0_#%08d", id));
        if (attachment instanceof FileAttachment) {
            writeFileAttachment((FileAttachment) attachment, id, att_dir);
        }
    }

    private void writeFileAttachment(FileAttachment attachment, int id, DirectoryEntry att_dir) throws IOException {
        List<? extends SubstGEntry> entries = Arrays.asList(
                new BinaryEntry("3701", attachment.getData()),
                new StringUTF16SubstgEntry("3703", attachment.getExtension()),
                new StringUTF16SubstgEntry("3704", attachment.getFilename()),
                new StringUTF16SubstgEntry("3707", attachment.getLongFilename()),
                new StringUTF16SubstgEntry("370e", attachment.getMimeTag())
        );

        for (SubstGEntry entry : entries) {
            entry.createEntry(att_dir);
        }

        List<PropType> props = new ArrayList<>();
        props.add(new PropPtypInteger32("3712", id));
        for (SubstGEntry entry : entries) {
            props.add(entry.getPropType());
        }

        byte[] bytes = createPropertiesEntryContent(props);

        createPropEntry(bytes, att_dir);
    }

    private static byte[] createPropertiesEntryContent(Collection<PropType> p_entries) {
        int offset = 8;
        int size = offset + p_entries.size() * 16;
        byte bytes[] = new byte[size];

        for (PropType prop : p_entries) {
            prop.writePropertiesEntry(bytes, offset);
            offset += 16;
        }

        return bytes;
    }
}
