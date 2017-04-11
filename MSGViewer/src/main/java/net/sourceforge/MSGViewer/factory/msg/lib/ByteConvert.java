/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.lib;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author martin
 */
public class ByteConvert {
    
        
    static public long convertByteArrayToLong(byte[] bytebuf) {

        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytebuf);

        return buffer.getLong(0);
    }        
    
    static public long convertByteArrayToLong(byte[] bytebuf, int offset) {

        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytebuf, offset, 8);

        return buffer.getLong(0);
    }    
}
