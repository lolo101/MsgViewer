package at.redeye.FrameWork.utilities;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.regex.Pattern;

public class HTMLColor {

    private static final Logger logger = LogManager.getLogger(HTMLColor.class);
    private static final Pattern COLOR_PATTERN = Pattern.compile("[0-9A-Fa-f]{6}");

    private static Color HTMLCode2Color(String colorString) {
        String s_color = StringUtils.strip(colorString, "# \"\t\n");

        if (!COLOR_PATTERN.matcher(s_color).matches()) {
            logger.debug("color string " + colorString + " ( stripped: " + s_color + ") is not a valid HTML Color");
            return null;
        }

        int r = Integer.parseInt(s_color.substring(0, 2), 16);
        int b = Integer.parseInt(s_color.substring(2, 4),16);
        int g = Integer.parseInt(s_color.substring(4, 6),16);

        return new Color( r, b, g );
    }

    public static Color loadLocalColor( Root root, DBConfig param )
    {
        String colorString = root.getSetup().getLocalConfig(param);

        return HTMLCode2Color( colorString );
    }
}
