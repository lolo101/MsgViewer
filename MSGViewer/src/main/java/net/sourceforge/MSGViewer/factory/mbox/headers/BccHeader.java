package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.RecipientType;

public class BccHeader extends RecipientHeader {
    public BccHeader() {
        super(RecipientType.BCC);
    }
}
