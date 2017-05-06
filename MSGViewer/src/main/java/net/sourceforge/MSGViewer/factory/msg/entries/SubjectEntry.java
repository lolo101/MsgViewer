package net.sourceforge.MSGViewer.factory.msg.entries;

/**
 *
 * @author martin
 */
public class SubjectEntry extends StringUTF16SubstgEntry
{
    public static final String NAME = "0037";

    public SubjectEntry(String subject )
    {
        super( NAME, subject );
    }
}
