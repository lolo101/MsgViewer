package at.redeye.FrameWork.base;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DialogsTest {

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