package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.Message;
import net.htmlparser.jericho.Source;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewerHelperTest {

    private final ViewerHelper helper = new ViewerHelper(new Root("test"));

    @Nested
    class PrepareImagesTest {
        @Test
        void should_strip_meta_element() {
            Source html = new Source("<html><head><meta charset=\"UTF-8\"><meta></head><body>Hello, World!</body></html>");

            String strippedHtml = helper.prepareImages(new Message(), html);

            assertEquals("<html><head>                            </head><body>Hello, World!</body></html>", strippedHtml);
        }

        @Test
        void should_strip_font_size_attribute() {
            Source html = new Source("<html><body><font size='10'>Hello, World!</font></body></html>");

            String strippedHtml = helper.prepareImages(new Message(), html);

            assertEquals("<html><body><font          >Hello, World!</font></body></html>", strippedHtml);
        }
    }

    @Nested
    class ExtractUrlTest {
        @Test
        void should_return_URI_path_when_no_attachment_in_message() throws IOException {
            URI uri = URI.create("file://host/dir/toto.msg");

            Path path = helper.extractUrl(uri, new Message());

            assertEquals(Path.of("/dir", "toto.msg"), path);
        }
    }
}