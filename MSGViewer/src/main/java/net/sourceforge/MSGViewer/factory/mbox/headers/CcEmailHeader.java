package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.RecipientType;

public class CcEmailHeader extends EmailHeader
{
    public CcEmailHeader()
    {
        super(RecipientType.CC);
    }
}
