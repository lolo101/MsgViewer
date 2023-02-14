package at.redeye.FrameWork.widgets.helpwindow;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpFileLoaderTest {
    @Test
    void should_replace_both_images_path() throws IOException {
        HelpFileLoader sut = new HelpFileLoader();
        String base = "/at/redeye/FrameWork/widgets/helpwindow";
        String module = "HelpFileLoaderTest";
        URL image1 = HelpFileLoaderTest.class.getResource("/at/redeye/FrameWork/widgets/helpwindow/image1.png");
        URL image2 = HelpFileLoaderTest.class.getResource("/at/redeye/FrameWork/widgets/helpwindow/image2.png");

        String result = sut.loadHelp(base, module);

        assertEquals("<html lang=\"en\"><img src=\"" + image1 + "\" alt=\"first image\"><img src=\"" + image2 + "\" alt=\"second image\"></html>", result);
    }
}