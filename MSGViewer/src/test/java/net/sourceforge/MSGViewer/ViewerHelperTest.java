package net.sourceforge.MSGViewer;

import com.auxilii.msgparser.Message;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewerHelperTest {

    private final ViewerHelper helper = new ViewerHelper(null);

    @Test
    void should_strip_meta_element() {
        byte[] html = "<html><head><meta charset=\"UTF-8\"><meta></head><body>Hello, World!</body></html>".getBytes(StandardCharsets.UTF_8);

        String strippedHtml = helper.prepareImages(new Message(), html);

        assertEquals("<html><head>                            </head><body>Hello, World!</body></html>", strippedHtml);
    }

    @Test
    void should_strip_font_size_attribute() {
        byte[] html = "<html><body><font size='10'>Hello, World!</font></body></html>".getBytes(StandardCharsets.UTF_8);

        String strippedHtml = helper.prepareImages(new Message(), html);

        assertEquals("<html><body><font          >Hello, World!</font></body></html>", strippedHtml);
    }
}