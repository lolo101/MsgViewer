package net.sourceforge.MSGViewer.factory.msg.entries;

import com.auxilii.msgparser.Pid;
import net.sourceforge.MSGViewer.factory.msg.properties.PropPtypString;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.auxilii.msgparser.Ptyp.PtypString;

/**
 * @author martin
 */
public class StringUTF16SubstgEntry extends SubStorageEntry {
    private final String value;

    public StringUTF16SubstgEntry(Pid tag, String value) {
        super(tag, PtypString);
        this.value = value == null ? "" : value;
    }

    @Override
    public PropType getPropType() {
        return new PropPtypString(getTag(), value.length());
    }

    @Override
    protected InputStream createEntryContent() {
        return switch (type) {
            case PtypString8 -> new ByteArrayInputStream(value.getBytes(StandardCharsets.ISO_8859_1));
            case PtypString -> new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_16LE));
            default -> throw new IllegalArgumentException(type.toString());
        };
    }

}
