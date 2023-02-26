package net.sourceforge.MSGViewer.CLIHelp;

import at.redeye.FrameWork.utilities.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CLIOption {
    private final String name;
    private final String short_description;
    private String long_description;

    public CLIOption(String name, String short_description, String long_description) {
        this.name = name;
        this.short_description = short_description;
        this.long_description = long_description;
    }

    public CLIOption(String name, String short_description) {
        this.name = name;
        this.short_description = short_description;
    }

    public String getName() {
        return name;
    }

    public void buildShortHelpText( StringBuilder sb, int fill_len_to_short_description, int max_line_len )
    {
        addWithFillSpacesTrailing( sb, name, fill_len_to_short_description );

        List<String> text = splitTextForMaximumLen(short_description, fill_len_to_short_description, max_line_len);

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

        List<String> text = splitTextForMaximumLen(long_description, fill_len_to_short_description, max_line_len);

        for (String s : text)
        {
            sb.append(s);
            sb.append("\n");
        }

    }

    private static void addWithFillSpacesTrailing(StringBuilder sb, String value, int len) {
        sb.append(value);
        sb.append(" ".repeat(Math.max(0, len - value.length())));
    }

    private static void addWithFillSpacesLeading(StringBuilder sb, int len) {
        sb.append(" ".repeat(Math.max(0, len - "".length())));
    }

    private static String addWithFillSpacesLeading(int len) {
        StringBuilder sb = new StringBuilder();
        addWithFillSpacesLeading(sb, len);
        return sb.toString();
    }

    private static List<String> splitTextForMaximumLen(String descr, int fill_len_to_short_description, int max_line_len) {
        int max_len = max_line_len - fill_len_to_short_description;

        String breaked_text = StringUtils.autoLineBreak(descr, max_len);

        String[] breaked_array = breaked_text.split("\n");

        return Arrays.stream(breaked_array)
                .map(s -> addWithFillSpacesLeading(fill_len_to_short_description) + s)
                .collect(toList());
    }
}
