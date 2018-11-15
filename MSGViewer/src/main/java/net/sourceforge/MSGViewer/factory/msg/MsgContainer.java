package net.sourceforge.MSGViewer.factory.msg;

import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypInteger32;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;
import net.sourceforge.MSGViewer.factory.msg.entries.StringUTF16SubstgEntry;
import net.sourceforge.MSGViewer.factory.msg.entries.SubstGEntry;
import com.auxilii.msgparser.RecipientEntry;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        bytes.position(HEADER_SIZE);

        for( PropType prop : properties )
        {
            prop.writePropertiesEntry(bytes);
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

        createPropEntry(bytes, root);
        createNamedEntry(root);
    }

    private static void createPropEntry(ByteBuffer bytes, DirectoryEntry root ) throws IOException
    {
        deleteEntryIfExists(root, NAME);

        InputStream buffer = new ByteArrayInputStream(bytes.array());
        root.createDocument(NAME, buffer);
    }

    private static void createNamedEntry(DirectoryEntry root ) throws IOException
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

    private static void writeRecipientEntry(DirectoryEntry root , RecipientEntry rec, int id) throws IOException
    {
        DirectoryEntry rec_dir = root.createDirectory(String.format("__recip_version1.0_#%08d", id));

        List<? extends SubstGEntry> entries = Arrays.asList(
                new StringUTF16SubstgEntry("3001", rec.getName()),
                new StringUTF16SubstgEntry("3002", "SMTP"),
                new StringUTF16SubstgEntry("3003", rec.getEmail())
        );

        for (SubstGEntry entry : entries) {
            entry.createEntry(rec_dir);
        }

        List<PropType> props = new ArrayList<>();
        props.add(new PropPtypInteger32("3000", id));
        props.add(new PropPtypInteger32("0C15", rec.getType().getValue()));
        for (SubstGEntry entry : entries) {
            props.add(entry.getPropType());
        }

        ByteBuffer bytes = createPropertiesEntryContent(props);

        createPropEntry(bytes, rec_dir);
    }

    private static void writeAttachment(DirectoryEntry root, Attachment attachment, int id) throws IOException {
        DirectoryEntry att_dir = root.createDirectory(String.format("__attach_version1.0_#%08d", id));
        if (attachment instanceof FileAttachment) {
            writeFileAttachment((FileAttachment) attachment, id, att_dir);
        }
    }

    private static void writeFileAttachment(FileAttachment attachment, int id, DirectoryEntry att_dir) throws IOException {
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

        ByteBuffer bytes = createPropertiesEntryContent(props);

        createPropEntry(bytes, att_dir);
    }

    private static ByteBuffer createPropertiesEntryContent(Collection<PropType> p_entries) {
        int size = 8 + p_entries.size() * 16;
        ByteBuffer bytes = ByteBuffer.allocate(size);
        bytes.position(8);

        for (PropType prop : p_entries) {
            prop.writePropertiesEntry(bytes);
        }

        return bytes;
    }
}
