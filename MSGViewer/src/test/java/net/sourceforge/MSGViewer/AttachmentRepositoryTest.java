package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttachmentRepositoryTest {
    @Test
    void should_name_file_with_attachment_content_id_and_extension() {
        String contentId = "content-id";

        Root root = new Root("test");
        AttachmentRepository attachmentRepository = new AttachmentRepository(root);
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setContentId(contentId);
        fileAttachment.setFilename("toto.txt");

        Path tempFile = attachmentRepository.getTempFile(fileAttachment);

        assertEquals(contentId + ".txt", tempFile.getFileName().toString());
    }

    @Test
    void should_name_message_with_attachment_hash() {
        Message message = new Message();

        Root root = new Root("test");
        AttachmentRepository attachmentRepository = new AttachmentRepository(root);
        MsgAttachment msgAttachment = new MsgAttachment(message);

        Path tempFile = attachmentRepository.getTempFile(msgAttachment);

        assertEquals(message.hashCode() + ".msg", tempFile.getFileName().toString());
    }
}