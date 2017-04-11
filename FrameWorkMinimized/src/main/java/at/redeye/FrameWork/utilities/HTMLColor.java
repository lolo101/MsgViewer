/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;

import java.awt.Color;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class HTMLColor {

    private static Logger logger = Logger.getLogger("HTMLColor");
                
    public static Color HTMLCode2Color( String colorString )
    {
        String s_color = StringUtils.strip(colorString, "# \"\t\n");
        
        if( s_color.matches("[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]") == false )
        {
            logger.debug("color string " + colorString + " ( stripped: " + s_color + ") is not a valid HTML Color");            
            return null;
        }
        
        Integer r = Integer.parseInt(s_color.substring(0, 2),16);
        Integer b = Integer.parseInt(s_color.substring(2, 4),16);
        Integer g = Integer.parseInt(s_color.substring(4, 6),16);
        
        return new Color( r, b, g );
    }
    
    public static Color loadLocalColor( Root root, DBConfig param )
    {
        String colorString = root.getSetup().getLocalConfig(param);
        
        return HTMLCode2Color( colorString );
    }

    public static String Color2HTML( Color color )
    {
        String res = "#";

        res += String.format("%02X", color.getRed() );
        res += String.format("%02X", color.getGreen() );
        res += String.format("%02X", color.getBlue() );

        return res;
    }
}
