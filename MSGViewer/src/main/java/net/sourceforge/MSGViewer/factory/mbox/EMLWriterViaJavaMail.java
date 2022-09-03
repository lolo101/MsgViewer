package net.sourceforge.MSGViewer.factory.mbox;

import at.redeye.FrameWork.base.Root;

public class EMLWriterViaJavaMail extends MBoxWriterViaJavaMail {

    public EMLWriterViaJavaMail(Root root) {
        super(root);
    }

    @Override
    public String getExtension() {
        return "eml";
    }
}
