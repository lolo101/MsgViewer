package at.redeye.FrameWork.base;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

class DialogsTest {
    @BeforeAll
    static void disableWhenHeadless() {
        assumeFalse(GraphicsEnvironment.isHeadless());
    }

    @Test
    void should_close_app_when_all_dialgs_closed() {
        Root root = spy(new Root("Dialogs root"));
        root.setDefaultLanguage("en");
        root.setBaseLanguage("en");
        doNothing().when(root).appExit();

        Dialogs dialogs = new Dialogs(root);
        BaseDialogBase dlg = new BaseDialog(root, "dialog title");
        dialogs.informWindowOpened(dlg);
        dialogs.informWindowClosed(dlg);

        verify(root).appExit();
    }
}