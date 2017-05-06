package net.sourceforge.MSGViewer.factory.msg.entries;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author martin
 */
public class RTFBodyTextEntry extends BinaryEntry
{
    public static final String NAME = "1009";

    public RTFBodyTextEntry(String text) throws UnsupportedEncodingException
    {
        super(NAME, text.getBytes("UTF-16LE"));
    }

    public RTFBodyTextEntry(byte[] bodyCompressesRTF) {
        super( NAME, bodyCompressesRTF);
    }
}
