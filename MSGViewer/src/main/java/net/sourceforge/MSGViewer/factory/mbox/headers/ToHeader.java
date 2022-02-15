package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.RecipientType;

public class ToHeader extends RecipientHeader {
    public ToHeader() {
        super(RecipientType.TO);
    }
}
