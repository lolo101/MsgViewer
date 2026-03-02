package net.sourceforge.MSGViewer;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.FileSystem;

import static com.google.common.jimfs.Configuration.unix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrepareImagesTest {

    private static final FileSystem fileSystem = Jimfs.newFileSystem("host", unix());

    @Mock
    private AttachmentRepository attachmentRepository;

    @Test
    void should_replace_images_sources_with_content_id() {
        Message message = messageWithAttachmentWithContentId();
        PrepareImages sut = new PrepareImages(attachmentRepository, message);

        String actual = sut.prepareImages("<img src=\"cid:content-id-1\"></img>");

        assertEquals("<img src=\"jimfs://host/tmp/xxx.tmp/long-filename-1\"></img>", actual);
    }

    @Test
    void should_replace_images_sources_with_relative_path() {
        Message message = messageWithAttachmentWithContentLocation();
        PrepareImages sut = new PrepareImages(attachmentRepository, message);

        String actual = sut.prepareImages("<img src=\"content-location-2\"></img>");

        assertEquals("<img src=\"jimfs://host/tmp/xxx.tmp/content-location-2\"></img>", actual);
    }

    private Message messageWithAttachmentWithContentId() {
        Message message = new Message();
        FileAttachment attachmentWithContentId = new FileAttachment();
        attachmentWithContentId.setContentId("content-id-1");
        message.addAttachment(attachmentWithContentId);
        when(attachmentRepository.getTempFile(attachmentWithContentId)).thenReturn(fileSystem.getPath("/tmp/xxx.tmp/long-filename-1"));
        return message;
    }

    private Message messageWithAttachmentWithContentLocation() {
        Message message = new Message();
        FileAttachment attachmentWithContentLocation = new FileAttachment();
        attachmentWithContentLocation.setContentLocation("content-location-2");
        message.addAttachment(attachmentWithContentLocation);
        when(attachmentRepository.getTempFile(attachmentWithContentLocation)).thenReturn(fileSystem.getPath("/tmp/xxx.tmp/content-location-2"));
        return message;
    }
}
