package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.RecipientType;

public class BccEmailHeader extends EmailHeader
{
    public BccEmailHeader()
    {
        super(RecipientType.BCC);
    }
}
