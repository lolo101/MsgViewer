package net.sourceforge.MSGViewer.factory.msg.entries;

/**
 *
 * @author martin
 */
public class BodyTextEntry extends StringUTF16SubstgEntry
{
    public static final String NAME = "1000";

    public BodyTextEntry( String text)
    {
        super( NAME, text);
    }

}
