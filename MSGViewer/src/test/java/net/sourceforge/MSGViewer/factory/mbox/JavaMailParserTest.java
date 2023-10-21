package net.sourceforge.MSGViewer.factory.mbox;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaMailParserTest {
    @ParameterizedTest
    @CsvSource({"/umlaute.mbox,test öööääüüü", "/Einladung Hochzeit.mbox,Liebe Grüße"})
    void should_parse_charset(String resource, String expected) throws Exception {
        URI uri = JavaMailParserTest.class.getResource(resource).toURI();
        Path file = Path.of(uri);
        JavaMailParser sut = new JavaMailParser(file);
        Message message = sut.parse();
        String bodyText = message.getBodyText();
        assertTrue(bodyText.contains(expected));
    }

    @Test
    void unnamed_attachments_should_be_named_unknown() throws Exception {
        URI uri = JavaMailParserTest.class.getResource("/unnamed-attachment.eml").toURI();
        Path file = Path.of(uri);
        JavaMailParser sut = new JavaMailParser(file);
        Message message = sut.parse();
        List<Attachment> attachments = message.getAttachments();
        assertThat(attachments)
                .element(0)
                .isInstanceOf(FileAttachment.class)
                .extracting(attachment -> ((FileAttachment) attachment).getLongFilename())
                .isEqualTo("unknown");
    }
}
