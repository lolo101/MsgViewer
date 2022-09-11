package net.sourceforge.MSGViewer.MSGNavigator;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Pid;
import com.auxilii.msgparser.Ptyp;
import net.sourceforge.MSGViewer.factory.msg.lib.ByteConvert;
import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hmef.CompressedRTF;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class ShowNode extends BaseDialog {

    private static final long serialVersionUID = 3130592579862038804L;
    private static final Pattern NAMED_PROPERTY_SUBSTORAGE = Pattern.compile("__substg1\\.0_\\d{4}0102");
    private static final int PROPATTR_MANDATORY = 0x00000001;
    private static final int PROPATTR_READABLE = 0x00000002;
    private static final int PROPATTR_WRITABLE = 0x00000004;
    private static final Map<String, String> PROPERTY_SETS = Map.ofEntries(
            entry("00020329-0000-0000-C000-000000000046", "PS_PUBLIC_STRINGS"),
            entry("00062008-0000-0000-C000-000000000046", "PSETID_Common"),
            entry("00062004-0000-0000-C000-000000000046", "PSETID_Address"),
            entry("00020386-0000-0000-C000-000000000046", "PS_INTERNET_HEADERS"),
            entry("00062002-0000-0000-C000-000000000046", "PSETID_Appointment"),
            entry("6ED8DA90-450B-101B-98DA-00AA003F1305", "PSETID_Meeting"),
            entry("0006200A-0000-0000-C000-000000000046", "PSETID_Log"),
            entry("41F28F13-83F4-4114-A584-EEDB5A6B0BFF", "PSETID_Messaging"),
            entry("0006200E-0000-0000-C000-000000000046", "PSETID_Note"),
            entry("00062041-0000-0000-C000-000000000046", "PSETID_PostRss"),
            entry("00062003-0000-0000-C000-000000000046", "PSETID_Task"),
            entry("4442858E-A9E3-4E80-B900-317A210CC15B", "PSETID_UnifiedMessaging"),
            entry("00020328-0000-0000-C000-000000000046", "PS_MAPI"),
            entry("71035549-0739-4DCB-9163-00F0580DBBDF", "PSETID_AirSync"),
            entry("00062040-0000-0000-C000-000000000046", "PSETID_Sharing"),
            entry("23239608-685D-4732-9C55-4C95CB4E8E33", "PSETID_XmlExtractedEntities"),
            entry("96357F7F-59E1-47D0-99A7-46515C183B54", "PSETID_Attachment"));

    private final int max_descr_lenght;

    public ShowNode(Root root, TreeNodeContainer cont) {
        super(root, root.MlM("Navigate:") + " " + cont.getEntry().getName());
        initComponents();

        int actual_pid_max_length = Arrays.stream(Pid.values())
                .mapToInt(pid -> pid.toString().length())
                .max()
                .orElse(0);
        max_descr_lenght = Math.max(20, actual_pid_max_length);

        if (cont.getEntry().isDocumentEntry()) {
            String name = cont.getEntry().getName();
            jTHex.setText(show_entry(name, cont));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTHex = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jTHex.setColumns(80);
        jTHex.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12)); // NOI18N
        jTHex.setRows(25);
        jScrollPane1.setViewportView(jTHex);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea jTHex;
    // End of variables declaration//GEN-END:variables

    private String show_entry(String name, TreeNodeContainer cont) {
        if (name.equals("__properties_version1.0")) {
            return show_properties_entry(cont);
        }
        if (name.equals(Ptyp.SUBSTORAGE_PREFIX + "00020102")) {
            return show_guid_stream(cont);
        }
        if (name.equals(Ptyp.SUBSTORAGE_PREFIX + "00030102")) {
            return show_entry_stream(cont);
        }
        if (name.equals(Ptyp.SUBSTORAGE_PREFIX + "00040102")) {
            return show_string_stream(cont);
        }
        if (NAMED_PROPERTY_SUBSTORAGE.matcher(name).matches() &&
                cont.getEntry().getParent().getName().equals("__nameid_version1.0")) {
            return show_nameid_stream(cont);
        }
        return show_data(cont);
    }

    private String show_properties_entry(TreeNodeContainer cont) {
        byte[] bytes = (byte[]) cont.getData();

        StringBuilder sb = new StringBuilder();
        sb.append("__properties_version1.0\n");
        sb.append("HEADER\n");
        sb.append("\tRESERVED 8 bytes (should by zero)\n\t\t");

        int offset = 0;

        for (int i = 0; i < 8; i++, offset++) {
            sb.append(formatByte(bytes[offset]));
        }

        boolean is_toplevel = cont.getEntry().getParent().getParent() == null;
        if (is_toplevel) {
            sb.append("\n\n\tNEXT Recipient ID 4 bytes \n\t\t");

            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }

            sb.append("\n\n\tNEXT Attachment ID 4 bytes \n\t\t");

            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }

            sb.append("\n\n\tRecipient Count 4 bytes \n\t\t");

            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }

            sb.append("\n\n\tAttachment Count 4 bytes \n\t\t");

            for (int i = 0; i < 4; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }

            sb.append("\n\n\tReserved 8 bytes (should by zero)\n\t\t");

            for (int i = 0; i < 8; i++, offset++) {
                sb.append(formatByte(bytes[offset]));
            }
        }

        sb.append("\n");

        for (; offset < bytes.length; offset += 16) {
            sb.append("\n");
            sb.append(dumpPropertyEntry(bytes, offset));
        }

        return sb.toString();
    }

    private static String formatByte(byte b) {
        return b == 0 ? "__ " : String.format("%02X ", b);
    }

    private static String formatByte0S(byte b) {
        return String.format("%02X", b);
    }

    private String dumpPropertyEntry(byte[] bytes, int offset) {
        StringBuilder sb = new StringBuilder();
        sb.append("TAG: ");

        StringBuilder tagname = new StringBuilder();

        // property tag
        for (int i = offset + 3; i >= offset; i--) {
            sb.append(formatByte0S(bytes[i]));
            tagname.append(formatByte0S(bytes[i]));
        }

        offset += 4;

        sb.append(printFlags(bytes[offset]));

        offset += 4;

        sb.append(" VALUE: ");

        int value_start_offset = offset;

        sb.append(formatQWordSpaced(bytes, offset));

        sb.append(" ");

        String lTagname = tagname.toString().toLowerCase();
        Ptyp tagtype = Ptyp.from(Integer.parseInt(lTagname.substring(4), 16));
        Pid descr = Pid.from(Integer.parseInt(lTagname.substring(0, 4), 16), tagtype);

        sb.append(StringUtils.rightPad(descr.toString(), max_descr_lenght));

        switch (tagtype) {
            case PtypString: {
                StringBuilder res = new StringBuilder();
                for (int i = value_start_offset + 3; i >= value_start_offset; i--) {
                    res.append(formatByte0S(bytes[i]));
                }
                int length = Integer.valueOf(res.toString(), 16);
                sb.append(" PtypString length: ");
                sb.append(length - 2);
                break;
            }
            case PtypBinary: {
                StringBuilder res = new StringBuilder();
                for (int i = value_start_offset + 3; i >= value_start_offset; i--) {
                    res.append(formatByte0S(bytes[i]));
                }
                int length = Integer.valueOf(res.toString(), 16);
                sb.append(" PtypBinary length: ");
                sb.append(length);
                break;
            }
            case PtypTime:
                sb.append(" PtypTime");
                long val = ByteConvert.convertByteArrayToLong(bytes, value_start_offset);
                sb.append(" ");
                sb.append(val);
                sb.append(": ");
                sb.append(new Date(MSTimeConvert.PtypeTime2Millis(val)));
                break;
            case PtypBoolean:
                sb.append(" boolean");
                break;
            case PtypInteger32: {
                StringBuilder res = new StringBuilder();
                for (int i = value_start_offset + 3; i >= value_start_offset; i--) {
                    res.append(formatByte0S(bytes[i]));
                }
                int length = Integer.valueOf(res.toString(), 16);
                sb.append(" PtypInteger32 value: ");
                sb.append(length);
                break;
            }
        }
        return sb.toString();
    }

    private static String printFlags(int b) {
        boolean mandatory = (b & PROPATTR_MANDATORY) != 0;
        boolean readable = (b & PROPATTR_READABLE) != 0;
        boolean writable = (b & PROPATTR_WRITABLE) != 0;
        return " FLAGS: "
                + (mandatory ? "M" : "_")
                + (readable ? "R" : "_")
                + (writable ? "W" : "_");
    }

    private static String formatQWordSpaced(byte[] bytes, int offset) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(String.format("%02X ", bytes[offset + i]));
        }
        return sb.toString();
    }


    /**
     * [MS-OXMSG] 2.2.3.1.1
     */
    private static String show_guid_stream(TreeNodeContainer cont) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = (byte[]) cont.getData();

        sb.append("GUID Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        int count = 0;

        for (int offset = 0; offset < bytes.length; offset += 16, count++) {

            sb.append("INDEX: ");
            sb.append(String.format("% 2d", count + 3));

            sb.append(" {");

            StringBuilder sb_prop_set = new StringBuilder();

            int voffset = offset;
            for (int i = voffset + 3; i >= voffset; i--) {
                sb_prop_set.append(formatByte0S(bytes[i]));
            }

            sb_prop_set.append("-");

            voffset = offset + 4;
            for (int i = voffset + 1; i >= voffset; i--) {
                sb_prop_set.append(formatByte0S(bytes[i]));
            }

            sb_prop_set.append("-");

            voffset = offset + 6;
            for (int i = voffset + 1; i >= voffset; i--) {
                sb_prop_set.append(formatByte0S(bytes[i]));
            }

            sb_prop_set.append("-");

            voffset = offset + 8;
            for (int i = voffset; i < voffset + 2; i++) {
                sb_prop_set.append(formatByte0S(bytes[i]));
            }

            sb_prop_set.append("-");

            voffset = offset + 10;
            for (int i = voffset; i < voffset + 6; i++) {
                sb_prop_set.append(formatByte0S(bytes[i]));
            }

            sb.append(sb_prop_set);
            sb.append("} ");
            sb.append(getPropertySetById(sb_prop_set.toString()));

            sb.append("\n");
        }


        return sb.toString();
    }

    /**
     * [MS-OXPROPS] 1.3.2
     */
    private static String getPropertySetById(String id) {
        return PROPERTY_SETS.getOrDefault(id, "");
    }

    /**
     * [MS-OXMSG] 2.2.3.1.2
     */
    private static String show_entry_stream(TreeNodeContainer cont) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = (byte[]) cont.getData();

        sb.append("Entry Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        for (int offset = 0; offset < bytes.length; offset += 8) {
            sb.append("LID / Offset in Stream: ");

            int voffset = offset;

            // property tag
            for (int i = voffset + 3; i >= voffset; i--) {
                sb.append(formatByte0S(bytes[i]));
            }

            voffset += 4;

            sb.append(" / ");

            for (int i = voffset + 3; i >= voffset; i--) {
                sb.append(formatByte0S(bytes[i]));
            }

            sb.append(" -> ");

            sb.append(show_index_and_kind(voffset, bytes));
        }

        return sb.toString();
    }

    /**
     * [MS-OXMSG] 2.2.3.1.3
     */
    private static String show_string_stream(TreeNodeContainer cont) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = (byte[]) cont.getData();

        sb.append("String Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        int offset = 0;

        while (offset < bytes.length) {

            sb.append("Offset: ");
            sb.append(String.format("%04X", offset));
            sb.append(" ");

            // get length 4 bytes
            StringBuilder s_lenght = new StringBuilder();

            for (int i = offset + 3; i >= offset; i--) {
                s_lenght.append(formatByte0S(bytes[i]));
            }

            offset += 4;

            long len = Long.valueOf(s_lenght.toString(), 16);

            sb.append(s_lenght);
            sb.append(" (").append(len).append(")");
            sb.append(": ");

            byte[] s_byte = new byte[(int) len];

            System.arraycopy(bytes, offset, s_byte, 0, (int) len);

            offset += len;

            sb.append(new String(s_byte, StandardCharsets.UTF_16LE));

            sb.append("\n");

            // spool forward to next 4 byte boundary

            offset += offset % 4;
        }

        return sb.toString();
    }

    private static String show_nameid_stream(TreeNodeContainer cont) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = (byte[]) cont.getData();

        sb.append("NAMEID Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        for (int offset = 0; offset < bytes.length; offset += 8) {

            sb.append("NAME/CRC-32: ");

            int voffset = offset;
            for (int i = voffset + 3; i >= voffset; i--) {
                sb.append(formatByte0S(bytes[i]));
            }

            voffset += 4;

            sb.append(" / ");
            sb.append(show_index_and_kind(voffset, bytes));
        }

        return sb.toString();
    }

    private static String show_data(TreeNodeContainer cont) {
        Object data = cont.getData();

        if (data instanceof String) {
            return (String) data;
        }
        if (data instanceof byte[]) {
            return show_data((byte[]) data);
        }
        return "";
    }

    private static String show_data(byte[] data) {
        return show_bytes_with_chars(data) +
                "\n\nASCII only\n" +
                show_ascii_chars(data) +
                "\n\n" +
                show_tnef(data) +
                "\n\n" +
                '[' + Hex.encodeHexString(data) + ']';
    }

    private static String show_bytes_with_chars(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {

            if (i > 0 && i % 20 == 0) {
                sb.append("\n");
            }

            if (data[i] == 0) {
                sb.append("__");
            } else {
                sb.append(String.format("%02X", data[i]));
            }

            sb.append(" ");

            if (data[i] > 30 && data[i] < 127) {
                sb.append(String.format("%c", (char) data[i]));
            } else {
                sb.append(" ");
            }

            sb.append("  ");
        }
        return sb.toString();
    }

    private static String show_ascii_chars(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {

            if (i > 0 && i % 20 == 0) {
                sb.append("\n");
            }

            if (data[i] > 30 && data[i] < 127) {
                sb.append(String.format("%c", (char) data[i]));
            }

        }
        return sb.toString();
    }

    private static String show_tnef(byte[] data) {
        try {
            byte[] decBytes = new CompressedRTF().decompress(new ByteArrayInputStream(data));
            return "decompressed TNEF\n\n" + new String(decBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger.info("failed decompressing: {}", ex.getLocalizedMessage());
            return "No TNEF content detected";
        }
    }

    private static String show_index_and_kind(int voffset, byte[] bytes) throws NumberFormatException {
        StringBuilder s_guid = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        // 15 Bytes GUID
        for (int i = voffset + 1; i >= voffset; i--) {
            s_guid.append(formatByte0S(bytes[i]));
        }

        voffset += 2;

        sb.append("INDEX: ");

        for (int i = voffset + 1; i >= voffset; i--) {
            sb.append(formatByte0S(bytes[i]));
        }

        sb.append(" GUID: ");

        long guid = Long.valueOf(s_guid.toString(), 16);

        boolean is_string = (guid & 0x01) > 0;

        guid >>= 1;

        sb.append(String.format("%04X", guid));
        sb.append(" ");
        sb.append(is_string ? "STRING " : "NUM ");
        sb.append("\n");
        return sb.toString();
    }
}
