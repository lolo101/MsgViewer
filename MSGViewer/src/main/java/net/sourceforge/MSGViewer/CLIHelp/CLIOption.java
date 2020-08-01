/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.CLIHelp;

import at.redeye.FrameWork.utilities.StringUtils;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public class CLIOption
{
    private String name;
    private String short_description;
    private String long_description = null;

    public CLIOption( String name, String short_description, String long_description )
    {
        this.name = name;
        this.short_description = short_description;
        this.long_description = long_description;
    }

    public CLIOption( String name, String short_description )
    {
        this.name = name;
        this.short_description = short_description;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return short_description;
    }

    public String getLongDescription() {
        return long_description;
    }

    public void buildShortHelpText( StringBuilder sb, int fill_len_to_short_description, int max_line_len )
    {
        addWithFillSpacesTrailing( sb, name, fill_len_to_short_description );

        ArrayList<String> text = splitTextForMaximumLen( short_description, fill_len_to_short_description, max_line_len );

        for( int i = 0; i < text.size(); i++ )
        {
            String s = text.get(i);

            if( i == 0 )
            {
                sb.append(s.substring(fill_len_to_short_description-1));
            }
            else
            {
               sb.append(s);
            }
        }

    }

    public void buildLongHelpText( StringBuilder sb, int fill_len_to_short_description, int max_line_len )
    {
        if( long_description == null )
            return;

        ArrayList<String> text = splitTextForMaximumLen( long_description, fill_len_to_short_description, max_line_len );

        for (String s : text)
        {
            sb.append(s);
            sb.append("\n");
        }

    }

    public static void addWithFillSpacesTrailing( StringBuilder sb, String value, int len )
    {
        sb.append(value);
        sb.append(" ".repeat(Math.max(0, len - value.length())));
    }

    public static void addWithFillSpacesLeading( StringBuilder sb, String value, int len )
    {
        sb.append(" ".repeat(Math.max(0, len - value.length())));
        sb.append(value);
    }

    public static String addWithFillSpacesLeading( String value, int len )
    {
        StringBuilder sb = new StringBuilder();
        addWithFillSpacesLeading( sb, value, len );
        return sb.toString();
    }

    public static ArrayList<String> splitTextForMaximumLen( String descr, int fill_len_to_short_description, int max_line_len )
    {
        int max_len = max_line_len - fill_len_to_short_description;

        String breaked_text = StringUtils.autoLineBreak(descr, max_len);

        String[] breaked_array = breaked_text.split("\n");

        ArrayList<String> ret = new ArrayList<>();

        for( String s : breaked_array )
        {
            ret.add(addWithFillSpacesLeading( "", fill_len_to_short_description) + s);
        }

        return ret;
    }
}
