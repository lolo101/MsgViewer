package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;

import java.nio.file.Path;

public class AttachmentRepository {
    private final Root root;

    public AttachmentRepository(Root root) {
        this.root = root;
    }

    public Path getTempFile(FileAttachment fatt) {
        return getTempFile(fatt.getContentId() + fatt.getExtension());
    }

    public Path getTempFile(MsgAttachment matt) {
        return getTempFile(matt.getMessage().hashCode() + ".msg");
    }

    private Path getTempFile(String fileName) {
        return root.getStorage().resolve(fileName);
    }
}
