package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import org.junit.jupiter.api.Test;

import static net.sourceforge.MSGViewer.ViewerPanel.FILE_NAME_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

class ViewerPanelTest {
    private String fileName;

    @Test
    void should_decode_url_to_local_file() {
        Root root = new Root("test app");
        root.setStartupArgs(new String[0]);
        ViewerPanel viewerPanel = new ViewerPanel(new BaseDialog(root, "test title"));
        String fileNameUrl = String.valueOf(ViewerPanelTest.class.getResource("/rtf.msg"));
        viewerPanel.addPropertyChangeListener(FILE_NAME_PROPERTY, evt -> fileName = (String) evt.getNewValue());

        viewerPanel.view(fileNameUrl);

        assertThat(fileNameUrl).startsWith("file:");
        assertThat(fileName).doesNotStartWith("file:");
    }
}
