package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.RecipientType;

public class CcHeader extends RecipientHeader {
    public CcHeader() {
        super(RecipientType.CC);
    }
}
