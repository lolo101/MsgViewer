/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer.rtfparser;

import at.redeye.FrameWork.utilities.StringUtils;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class ConvertCharset
{
    private static final Logger logger = Logger.getLogger(ConvertCharset.class.getName());

    static String convertCharacter( String characterSet, String text )
    {
        if( characterSet.isEmpty() )
            return text;

        byte[] bytes = new byte[1];

        try {
            bytes[0] = (byte) Integer.parseInt(text,16);
        } catch( NumberFormatException ex ) {
            logger.error("character set: " + characterSet + " hey string '" + text + "'");
            logger.error(StringUtils.exceptionToString(ex));
        }

        try {
            String res = new String(bytes, "CP" + characterSet);
            return res;
        } catch (UnsupportedEncodingException ex) {
            logger.error("character set: " + characterSet + " hey string '" + text + "'");
            logger.error(StringUtils.exceptionToString(ex));
            return text;
        }
    }
}
