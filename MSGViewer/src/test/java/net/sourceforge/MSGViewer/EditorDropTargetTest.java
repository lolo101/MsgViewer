package net.sourceforge.MSGViewer;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class EditorDropTargetTest {

    @Test
    void drop_multiple_messages() {
        LoadMessageInterface main_win = mock(LoadMessageInterface.class);
        EditorDropTarget sut = new EditorDropTarget(main_win, new JEditorPane());

        DropTargetDragEvent dragEvent = givenFileListDragEvent();
        sut.dragEnter(dragEvent);

        DropTargetDropEvent dropEvent = givenFileListDropEvent();
        sut.drop(dropEvent);

        verify(main_win).loadMessage("a");
        verify(main_win).loadMessage("b");
    }

    private static DropTargetDropEvent givenFileListDropEvent() {
        DropTargetDropEvent dropEvent = mock(DropTargetDropEvent.class);
        when(dropEvent.getDropAction()).thenReturn(DnDConstants.ACTION_COPY);
        when(dropEvent.getTransferable()).thenReturn(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[0];
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return false;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) {
                return Arrays.asList(new File("a"), new File("b"));
            }
        });
        return dropEvent;
    }

    private static DropTargetDragEvent givenFileListDragEvent() {
        DropTargetDragEvent dragEvent = mock(DropTargetDragEvent.class);
        when(dragEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)).thenReturn(true);
        when(dragEvent.getSourceActions()).thenReturn(DnDConstants.ACTION_COPY_OR_MOVE);
        return dragEvent;
    }
}