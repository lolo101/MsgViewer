package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.RecipientType;

public class ToEmailHeader extends EmailHeader
{
    public ToEmailHeader()
    {
        super(RecipientType.TO);
    }
}
