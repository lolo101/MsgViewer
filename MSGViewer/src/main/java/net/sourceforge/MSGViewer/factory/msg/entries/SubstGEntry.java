package net.sourceforge.MSGViewer.factory.msg.entries;

import com.auxilii.msgparser.Pid;
import com.auxilii.msgparser.Ptyp;
import net.sourceforge.MSGViewer.factory.msg.properties.PropType;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

import java.io.IOException;
import java.io.InputStream;

public abstract class SubstGEntry {
    protected final Ptyp type;
    protected final Pid tag;

    public SubstGEntry(Pid tag, Ptyp type) {
        this.tag = tag;
        this.type = type;
    }

    public Ptyp getType() {
        return type;
    }

    public Pid getTag() {
        return tag;
    }

    public abstract PropType getPropType();

    public final void createEntry(DirectoryEntry dir) throws IOException {
        InputStream stream = createEntryContent();
        dir.createDocument(String.format(Ptyp.SUBSTORAGE_PREFIX + "%04X%04X", tag.id, type.id), stream);
    }

    protected abstract InputStream createEntryContent() throws IOException;
}
