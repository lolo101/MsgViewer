package net.sourceforge.MSGViewer;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrepareImagesTest {
    @Mock
    private ViewerHelper fileRepository;

    @Test
    void should_replace_images_sources_with_content_id() {
        Message message = messageWithAttachmentWithContentId();
        PrepareImages sut = new PrepareImages(fileRepository, message);

        String actual = sut.prepareImages("<img src=\"cid:content-id-1\"></img>");

        assertEquals("<img src=\"file:/tmp/xxx.tmp/long-filename-1\"></img>", actual);
    }

    @Test
    void should_replace_images_sources_with_relative_path() {
        Message message = messageWithAttachmentWithContentLocation();
        PrepareImages sut = new PrepareImages(fileRepository, message);

        String actual = sut.prepareImages("<img src=\"content-location-2\"></img>");

        assertEquals("<img src=\"file:/tmp/xxx.tmp/content-location-2\"></img>", actual);
    }

    private Message messageWithAttachmentWithContentId() {
        Message message = new Message();
        FileAttachment attachmentWithContentId = new FileAttachment();
        attachmentWithContentId.setContentId("content-id-1");
        message.addAttachment(attachmentWithContentId);
        when(fileRepository.getTempFile(attachmentWithContentId)).thenReturn(new File("/tmp/xxx.tmp/long-filename-1"));
        return message;
    }

    private Message messageWithAttachmentWithContentLocation() {
        Message message = new Message();
        FileAttachment attachmentWithContentLocation = new FileAttachment();
        attachmentWithContentLocation.setContentLocation("content-location-2");
        message.addAttachment(attachmentWithContentLocation);
        when(fileRepository.getTempFile(attachmentWithContentLocation)).thenReturn(new File("/tmp/xxx.tmp/content-location-2"));
        return message;
    }
}
