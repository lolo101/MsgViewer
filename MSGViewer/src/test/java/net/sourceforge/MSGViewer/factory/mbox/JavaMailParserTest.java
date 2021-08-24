package net.sourceforge.MSGViewer.factory.mbox;

import com.auxilii.msgparser.Message;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.nio.file.Path;

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
}
