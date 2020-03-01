package net.sourceforge.MSGViewer.MSGNavigator;

import com.auxilii.msgparser.Pid;
import com.auxilii.msgparser.Ptyp;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyParser {
    private final DocumentEntry entry;

    private final int max_id_lenght;
    private final int max_type_lenght;

    private final List<String> props = new ArrayList<>();

    public PropertyParser(DocumentEntry entry) throws IOException {
        this.entry = entry;

        max_id_lenght = Arrays.stream(Pid.values())
                .map(Pid::toString)
                .mapToInt(String::length)
                .max()
                .getAsInt();
        max_type_lenght = Arrays.stream(Ptyp.values())
                .map(Ptyp::toString)
                .mapToInt(String::length)
                .max()
                .getAsInt();

        parse();
    }

    final void parse() throws IOException {
        boolean is_toplevel = entry.getParent().getParent() == null;
        boolean is_msg = is_toplevel || entry.getParent().getName().equals("__substg1.0_3701000D");

        try (DocumentInputStream in = new DocumentInputStream(entry)) {
            // RESERVED 8 bytes (should by zero)
            in.skip(8);
            if (is_msg) {
                int nextRecipientId = in.readInt();
                int nextAttachmentId = in.readInt();
                int recipientCount = in.readInt();
                int attachmentCount = in.readInt();

                if (is_toplevel) {
                    // RESERVED 8 bytes (should by zero)
                    in.skip(8);
                }
            }

            while (in.available() > 0) {
                parsePropertyEntry(in);
            }
        }
    }

    private void parsePropertyEntry(DocumentInputStream in) {
        int tag = in.readInt();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%08X ", tag));

        int flags = in.readInt();
        sb.append((flags & 0x0001) > 0 ? "M" : "_");
        sb.append((flags & 0x0002) > 0 ? "R" : "_");
        sb.append((flags & 0x0004) > 0 ? "W" : "_");
        sb.append(" ");

        Pid id = Pid.from(tag >> 16);
        Ptyp typ = Ptyp.from(tag & 0xffff);
        sb.append(StringUtils.rightPad(id.toString(), max_id_lenght));
        sb.append(" ");
        sb.append(StringUtils.rightPad(typ.toString(), max_type_lenght));

        if (typ.variableLength || typ.multipleValued) {
            int size = in.readInt();
            int reserved = in.readInt();
            sb.append(" size: ").append(size);
        } else {
            sb.append(" value: ").append(typ.convert(in));
        }

        props.add(sb.toString());
    }

    public List<String> getPropertyTags() {
        return props;
    }
}
