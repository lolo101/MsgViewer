package net.sourceforge.MSGViewer.rtfparser;

import at.redeye.FrameWork.utilities.StringUtils;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

public class ConvertCharset
{
    private static final Logger logger = Logger.getLogger(ConvertCharset.class.getName());

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
