package net.sourceforge.MSGViewer.rtfparser;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;

public class ConvertCharset
{
    private static final Logger logger = LogManager.getLogger(ConvertCharset.class);

    static String convertCharacter( String characterSet, String text )
    {
        if( characterSet.isEmpty() )
            return text;

        String hexa = text.replace("\\'", "");

        byte[] bytes = new byte[hexa.length() / 2];

        try {
            for (int i = 0; i < bytes.length; ++i) {
                bytes[i] = (byte) Integer.parseInt(hexa.substring(2 * i, 2 * i + 2), 16);
            }
        } catch( NumberFormatException ex ) {
            logger.error("character set: " + characterSet + " hey string '" + text + "'");
            logger.error(StringUtils.exceptionToString(ex));
        }

        try {
            return new String(bytes, "CP" + characterSet);
        } catch (UnsupportedEncodingException ex) {
            logger.error("character set: " + characterSet + " hey string '" + text + "'");
            logger.error(StringUtils.exceptionToString(ex));
            return text;
        }
    }
}
