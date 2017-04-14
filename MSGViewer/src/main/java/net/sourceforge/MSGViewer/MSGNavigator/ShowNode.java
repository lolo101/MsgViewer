package net.sourceforge.MSGViewer.MSGNavigator;

import java.io.ByteArrayInputStream;

import net.sourceforge.MSGViewer.factory.msg.lib.MSTimeConvert;
import net.sourceforge.MSGViewer.factory.msg.lib.ByteConvert;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import net.sourceforge.MSGViewer.MSGNavigator.MSGNavigator.TreeNodeContainer;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hmef.CompressedRTF;

/**
 *
 * @author martin
 */
public class ShowNode extends BaseDialog {

    private static final long serialVersionUID = 3130592579862038804L;

    private HashMap <String,String> props;
    private int max_descr_lenght = 20;

    public ShowNode(Root root, TreeNodeContainer cont,  HashMap <String,String> props)
    {
        super( root, root.MlM("Navigate:") + " " + cont.getEntry().getName());
        initComponents();

        this.props = props;

        for( String descr : props.values() ) {
            if( descr.length() > max_descr_lenght ) {
                max_descr_lenght = descr.length();
            }
        }

        if( cont.getEntry().isDocumentEntry() )
        {
            if( cont.getEntry().getName().equals("__properties_version1.0") )
            {
                show_properties_entry(cont);
            }
            else if( cont.getEntry().getName().equals("__substg1.0_00030102") )
            {
                show_entry_stream(cont);
            }
            else if( cont.getEntry().getName().equals("__substg1.0_00040102") )
            {
                show_string_stream(cont);
            }
            else if( cont.getEntry().getName().equals("__substg1.0_00020102") )
            {
                show_guid_stream(cont);
            }
            else if( cont.getEntry().getName().matches("__substg1\\.0_[0-9][0-9][0-9][0-9]0102") &&
                   cont.getEntry().getParent().getName().equals("__nameid_version1.0") )
            {
                show_nameid_stream(cont);

            } else  {

                Object data = cont.getData();

                if( data instanceof String )
                {
                    jTHex.setText((String)data);
                }
                else if( data instanceof byte[] )
                {
                    StringBuilder sb = new StringBuilder();
                    byte bytes[] = (byte[]) data;

                    for( int i = 0; i < bytes.length; i++ ) {

                        if( i > 0 && i % 20 == 0 ) {
                            sb.append("\n");
                        }

                        if( bytes[i] == 0 ) {
                            sb.append("__");
                        } else {
                            sb.append(String.format("%02X", bytes[i]));
                        }

                        sb.append(" ");

                        if( bytes[i] > 30 && bytes[i] < 127 ) {
                            sb.append(String.format("%c", (char)bytes[i]));
                        } else {
                            sb.append(" ");
                        }

                        sb.append("  ");
                    }

                    sb.append("\n\nASCII only\n");

                    for( int i = 0; i < bytes.length; i++ ) {

                        if( i > 0 && i % 20 == 0 ) {
                            sb.append("\n");
                        }

                        if( bytes[i] > 30 && bytes[i] < 127 ) {
                            sb.append(String.format("%c", (char)bytes[i]));
                        }

                    }

                    try {
                        byte decBytes[] = new CompressedRTF().decompress(new ByteArrayInputStream(bytes));
                        sb.append("\n\ndecompressed TNEF\n\n");
                        sb.append(new String(decBytes));
                    } catch( Exception ex ) {
                        logger.error("failed decompressing", ex);
                    }

                    sb.append("\n\n");
                    sb.append('[').append(Hex.encodeHexString(bytes)).append(']');

                    jTHex.setText(sb.toString());
                }
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTHex = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jTHex.setColumns(20);
        jTHex.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTHex.setRows(5);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTHex;
    // End of variables declaration//GEN-END:variables

    private void show_properties_entry(TreeNodeContainer cont)
    {

        StringBuilder sb = new StringBuilder();
        byte bytes[] = (byte[]) cont.getData();

        sb.append("__properties_version1.0\n");
        sb.append("HEADER\n");
        sb.append("\tRESERVED 8 bytes (should by zero)\n\t\t");

        int offset = 0;

        for( int i = 0; i < 8; i++, offset++) {
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

        for( ; offset < bytes.length; offset += 16) {
            sb.append("\n");
            dumpPropertyEntry( sb, bytes, offset );
        }

        jTHex.setText(sb.toString());
    }

    private String formatByte( byte b )
    {
        return b == 0 ? "__ " : String.format("%02X ", b);
    }

    private String formatByte0( byte b )
    {
        return String.format("%02X ", b);
    }

    private String formatByte0S( byte b )
    {
        return String.format("%02X", b);
    }

    private void dumpPropertyEntry(StringBuilder sb, byte bytes[], int offset)
    {
        sb.append("TAG: ");

        String tagname = "";

        // property tag
        for( int i = offset + 3; i >= offset; i-- ) {
            sb.append(formatByte0S(bytes[i]));
            tagname += formatByte0S(bytes[i]);
        }

        offset += 4;

        sb.append(" FLAGS: ");
/*
        for( int i = offset; i < offset + 4; i++ )
            sb.append(formatByte0(bytes[i]));
  */
        if( (bytes[offset] & 0001) > 0 ) {
            sb.append("M");
        } else {
            sb.append("_");
        }

        if( (bytes[offset] & 0002) > 0 ) {
            sb.append("R");
        } else {
            sb.append("_");
        }

        if( (bytes[offset] & 0004) > 0 ) {
            sb.append("W");
        } else {
            sb.append("_");
        }

        offset += 4;

        sb.append(" VALUE: ");

        int value_start_offset = offset;

        for( int i = 0; i < 8; i++, offset++ ) {
            sb.append(formatByte0(bytes[offset]));
        }

        sb.append(" ");

        String descr = StringUtils.defaultString(props.get(tagname.toLowerCase().substring(0,4)));
        sb.append(StringUtils.rightPad(descr,max_descr_lenght));

        String tagtype = tagname.toLowerCase().substring(4);

        switch (tagtype) {
            case "001f":
                {
                    String res = "";
                    for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
                        res += formatByte0S(bytes[i]);
                    }        int length = Integer.valueOf(res, 16);
                    sb.append(" PtypString length: ");
                    sb.append(String.valueOf(length - 2));
                    break;
                }
            case "0102":
                {
                    String res = "";
                    for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
                        res += formatByte0S(bytes[i]);
                    }        int length = Integer.valueOf(res, 16);
                    sb.append(" PtypBinary length: ");
                    sb.append(String.valueOf(length));
                    break;
                }
            case "0040":
                sb.append(" PtypTime");
                /*
                byte buf[] = new byte[8];

                for( int i = 0; i < buf.length; i++ )
                buf[i] = bytes[value_start_offset++];
                *
                */

                long val = ByteConvert.convertByteArrayToLong(bytes,value_start_offset);
                sb.append(" ");
                sb.append(val);
                sb.append(": ");
                sb.append(new Date(MSTimeConvert.PtypeTime2Millis(val)).toString());
                ;
                break;
            case "000b":
                sb.append(" boolean");
                break;
            case "0003":
                {
                    String res = "";
                    for( int i = value_start_offset + 3; i >= value_start_offset; i-- ) {
                        res += formatByte0S(bytes[i]);
                    }        int length = Integer.valueOf(res, 16);
                    sb.append(" PtypInteger32 value: ");
                    sb.append(length);
                    break;
                }
        }
    }


    private void show_string_stream(TreeNodeContainer cont)
    {
        StringBuilder sb = new StringBuilder();
        byte bytes[] = (byte[]) cont.getData();

        sb.append("String Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        int offset = 0;

        while (offset < bytes.length) {

            sb.append("Offset: ");
            sb.append(String.format("%04X", offset));
            sb.append(" ");

            // get length 4 bytes
            String s_lenght = "";

            for (int i = offset + 3; i >= offset; i--) {
                s_lenght += formatByte0S(bytes[i]);
            }

            offset += 4;

            long len = Long.valueOf(s_lenght, 16);

            sb.append(s_lenght);
            sb.append(" (").append(len).append(")");
            sb.append(": ");

            // +2 nullterminating bytes to be for sure
            byte s_byte[] = new byte[(int) len];

            for (int i = offset, count = 0; i < offset + len; i++, count++) {
                s_byte[count] = bytes[i];
                // sb.append(formatByte(bytes[i]));
            }

            offset += len;

            try {
                sb.append(new String(s_byte, "UTF-16LE"));
            } catch (UnsupportedEncodingException ex) {
                logger.error(ex, ex);
            }

            sb.append("\n");

            // spool forward to next 4 byte boundary

            offset += offset % 4;
        }

        jTHex.setText(sb.toString());
    }

    private void show_guid_stream(TreeNodeContainer cont) {
        StringBuilder sb = new StringBuilder();
        byte bytes[] = (byte[]) cont.getData();

        sb.append("GUID Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        int count=0;

        for (int offset = 0; offset < bytes.length; offset += 16, count++) {

            sb.append("INDEX: ");
            sb.append(String.format("% 2d",count+3));

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


        jTHex.setText(sb.toString());
    }

    private String getPropertySetById( String id )
    {
        switch(id) {
            case "00020386-0000-0000-C000-000000000046":
                return "PS_INTERNET_HEADERS";
            case "00020329-0000-0000-C000-000000000046":
                return "PS_PUBLIC_STRINGS";
            case "00062008-0000-0000-C000-000000000046":
                return "PSETID_Common";
            case "00062004-0000-0000-C000-000000000046":
                return "PSETID_Address";
            case "00062002-0000-0000-C000-000000000046":
                return "PSETID_Appointment";
            case "6ED8DA90-450B-101B-98DA-00AA003F1305":
                return "PSETID_Meeting";
            case "0006200A-0000-0000-C000-000000000046":
                return "PSETID_Log";
            case "41F28F13-83F4-4114-A584-EEDB5A6B0BFF":
                return "PSETID_Messaging";
            case "0006200E-0000-0000-C000-000000000046":
                return "PSETID_Note";
            case "00062041-0000-0000-C000-000000000046":
                return "PSETID_PostRss";
            case "00062003-0000-0000-C000-000000000046":
                return "PSETID_Task";
            case "4442858E-A9E3-4E80-B900-317A210CC15B":
                return "PSETID_UnifiedMessaging";
            case "00020328-0000-0000-C000-000000000046":
                return "PS_MAPI";
            case "71035549-0739-4DCB-9163-00F0580DBBDF":
                return "PSETID_AirSync";
            case "00062040-0000-0000-C000-000000000046":
                return "PSETID_Sharing";
            default:
                return "";
        }
    }

    private void show_entry_stream(TreeNodeContainer cont)
    {
        StringBuilder sb = new StringBuilder();
        byte bytes[] = (byte[]) cont.getData();

        sb.append("Entry Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        for (int offset = 0; offset < bytes.length; offset += 8) {
            sb.append("Offset in Stream: ");

            int voffset = offset;

            // property tag
            for (int i = voffset + 3; i >= voffset; i--) {
                sb.append(formatByte0S(bytes[i]));
            }

            voffset += 4;

            sb.append(" (in HEX) ");

            for (int i = voffset + 3; i >= voffset; i--) {
                sb.append(formatByte0S(bytes[i]));
            }

            appendGuid(voffset, bytes, sb);
        }

        jTHex.setText(sb.toString());
    }

    private void show_nameid_stream(TreeNodeContainer cont) {
        StringBuilder sb = new StringBuilder();
        byte bytes[] = (byte[]) cont.getData();

        sb.append("NAMEID Stream ");
        sb.append(cont.getEntry().getName());
        sb.append("\n\n");

        for (int offset = 0; offset < bytes.length; offset += 8) {

            sb.append( "NAME/CRC-32: ");

            int voffset = offset;
            for (int i = voffset + 3; i >= voffset; i--) {
                sb.append(formatByte0S(bytes[i]));
            }

            voffset += 4;

            appendGuid(voffset, bytes, sb);
        }

        jTHex.setText(sb.toString());
    }

    private void appendGuid(int voffset, byte[] bytes, StringBuilder sb) throws NumberFormatException {
        String s_guid = "";

        // 15 Bytes GUID
        for (int i = voffset + 1; i >= voffset; i--) {
            s_guid += formatByte0S(bytes[i]);
        }

        voffset += 2;

        sb.append(" INDEX: ");

        for (int i = voffset + 1; i >= voffset; i--) {
            sb.append(formatByte0S(bytes[i]));
        }

        sb.append(" GUID: ");

        long guid = Long.valueOf(s_guid,16);

        boolean is_string = (guid & 0x01) > 0;

        guid >>= 1;

        sb.append(String.format("%04X",guid));
        sb.append(" ");
        sb.append(is_string ? "STRING " : "NUM ");
        sb.append("\n");
    }
}
