package at.redeye.FrameWork.base;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseDialogBaseHelperTest {
    @Test
    void should_set_parent_bounds_such_as_pointer_is_included_when_pointer_outside_default_position() {
        Root root = new Root("App Name");
        BaseDialogBase parent = Mockito.mock(BaseDialogBase.class);
        int mouseX = 123;
        int mouseY = 231;
        when(parent.mousePosition()).thenReturn(new Point(mouseX, mouseY));

        new BaseDialogBaseHelper(parent, root, "title", null, true);

        verify(parent).setBounds(mouseX - 100, mouseY - 100, 0, 0);
    }
}
