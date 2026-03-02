package net.sourceforge.MSGViewer.factory.mbox;

import net.sourceforge.MSGViewer.AttachmentRepository;

public class EMLWriterViaJavaMail extends MBoxWriterViaJavaMail {

    public EMLWriterViaJavaMail(AttachmentRepository attachmentRepository) {
        super(attachmentRepository);
    }

    @Override
    protected String getExtension() {
        return ".eml";
    }
}
