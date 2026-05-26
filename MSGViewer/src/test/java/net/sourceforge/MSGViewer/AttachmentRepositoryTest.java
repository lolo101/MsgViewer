package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttachmentRepositoryTest {
    @Test
    void should_name_file_with_data_hash() {
        Root root = new Root("test");
        AttachmentRepository attachmentRepository = new AttachmentRepository(root);
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setData("Coucou les gars !".getBytes(StandardCharsets.UTF_8));

        Path tempFile = attachmentRepository.getTempFile(fileAttachment);

        assertEquals("0de1997f0708e76553b03d40ff8420e351add892", tempFile.getFileName().toString());
    }

    @Test
    void should_name_empty_file_with_empty_data_hash() {
        Root root = new Root("test");
        AttachmentRepository attachmentRepository = new AttachmentRepository(root);
        FileAttachment fileAttachment = new FileAttachment();

        Path tempFile = attachmentRepository.getTempFile(fileAttachment);

        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", tempFile.getFileName().toString());
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