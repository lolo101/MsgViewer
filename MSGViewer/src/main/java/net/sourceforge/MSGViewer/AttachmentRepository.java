package net.sourceforge.MSGViewer;

import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;

import java.nio.file.Path;

public interface AttachmentRepository {
    Path getTempFile(FileAttachment fatt);

    Path getTempFile(MsgAttachment matt);
}
