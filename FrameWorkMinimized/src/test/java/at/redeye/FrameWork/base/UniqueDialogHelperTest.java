package at.redeye.FrameWork.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class UniqueDialogHelperTest {

    public static final LocalRoot ROOT = new LocalRoot("test");

    @Test
    void should_return_dialog_instance() {
        UniqueDialogHelper sut = new UniqueDialogHelper();
        BaseDialogBase dialog = new BaseDialogDialog(ROOT, "title");

        BaseDialogBase returned = sut.invokeUniqueDialog(dialog);

        assertSame(dialog, returned);
    }

    @Test
    void should_register_close_listener() {
        UniqueDialogHelper sut = new UniqueDialogHelper();
        BaseDialogBase dialog = mock(BaseDialogBase.class);

        sut.invokeUniqueDialog(dialog);

        verify(dialog).registerOnCloseListener(any());
    }

    @Nested
    class WhenDialogAlreadyInvoked {

        public static final String IDENTIFIER = "identifier";
        private final UniqueDialogHelper sut = new UniqueDialogHelper();
        private final BaseDialogBase dialog = new BaseDialogDialog(ROOT, IDENTIFIER);

        @BeforeEach
        void before() {
            sut.invokeUniqueDialog(dialog);
        }

        @Test
        void should_close_duplicate_dialog() {
            BaseDialogBase dupDialog = mock(BaseDialogBase.class);
            when(dupDialog.getUniqueDialogIdentifier()).thenReturn(IDENTIFIER);

            sut.invokeUniqueDialog(dupDialog);

            verify(dupDialog).close();
        }

        @Test
        void should_retrun_first_dialog() {
            BaseDialogBase dupDialog = mock(BaseDialogBase.class);
            when(dupDialog.getUniqueDialogIdentifier()).thenReturn(IDENTIFIER);

            BaseDialogBase returned = sut.invokeUniqueDialog(dupDialog);

            assertSame(dialog, returned);
        }
    }
}