package net.sourceforge.MSGViewer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class EditorDropTarget implements DropTargetListener {
    private static final Logger logger = LogManager.getLogger(EditorDropTarget.class);
    private final MessageView messageView;
    private Color backgroundColor;
    private boolean draggingContent;
    private boolean draggingFile;

    public EditorDropTarget(MessageView messageView) {
        this.messageView = messageView;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

        logger.debug("dragEnter");

        // Get the type of object being transferred and determine
        // whether it is appropriate.
        checkTransferType(dtde);

        if (acceptsDrag(dtde))
            dragEnter((JTextComponent) dtde.getDropTargetContext().getComponent());
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        logger.debug("dragOver");
        if (acceptsDrag(dtde))
            dragOver((JTextComponent) dtde.getDropTargetContext().getComponent(), dtde.getLocation());
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        acceptsDrag(dtde);
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        logger.debug("dragExit");
        dragExit((JTextComponent) dte.getDropTargetContext().getComponent());
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        logger.debug("drop");
        dragExit((JTextComponent) dtde.getDropTargetContext().getComponent());

        // Check the drop action
        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            // Accept the drop and get the transfer data
            dtde.acceptDrop(dtde.getDropAction());
            Transferable transferable = dtde.getTransferable();

            try {
                boolean result;

                if (draggingFile) {
                    result = dropFile(transferable);
                } else {
                    result = dropContent(transferable, dtde.getCurrentDataFlavors());
                }

                dtde.dropComplete(result);
                logger.info("Drop completed, success: {}", result);
            } catch (Exception e) {
                logger.info("Exception while handling drop {}", String.valueOf(e));
                dtde.rejectDrop();
            }
        } else {
            logger.info("Drop target rejected drop");
            dtde.dropComplete(false);
        }

    }

    private boolean acceptsDrag(DropTargetDragEvent dtde) {
        int sourceActions = dtde.getSourceActions();
        boolean acceptedDrag = (draggingFile || draggingContent)
                && (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
        if (acceptedDrag) {
            dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        } else {
            dtde.rejectDrag();
        }
        return acceptedDrag;
    }

    private void dragEnter(JTextComponent component) {
        if (draggingFile) {
            backgroundColor = component.getBackground();
            Color feedbackColor = backgroundColor.darker();
            component.setBackground(feedbackColor);
            component.repaint();
        } else {
            component.getCaret().setVisible(true);
        }
    }

    private void dragOver(JTextComponent pane, Point location) {
        if (draggingContent) {
            pane.setCaretPosition(pane.viewToModel2D(location));
        }
    }

    private void dragExit(JTextComponent component) {
        if (draggingFile) {
            component.setBackground(backgroundColor);
            component.repaint();
        } else {
            component.getCaret().setVisible(false);
        }
    }

    private void checkTransferType(DropTargetDragEvent dtde) {
        draggingFile = dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        draggingContent = dtde.isDataFlavorSupported(DataFlavor.stringFlavor);

        logger.info("Dragging a file - {}", draggingFile);
        logger.info("Dragging content - {}", draggingContent);
    }

    // This method handles a drop for a list of files
    private boolean dropFile(Transferable transferable) throws IOException,
            UnsupportedFlavorException {
        List<File> fileList = (List<File>) transferable
                .getTransferData(DataFlavor.javaFileListFlavor);
        fileList.forEach(transferFile -> {
            logger.info("Opening file {}", transferFile);
            messageView.view(transferFile.getPath());
        });

        return true;
    }

    // This method handles a drop with data content
    private boolean dropContent(Transferable transferable, DataFlavor[] flavors) {

        try {
            // Check for a match with the current content type
            DataFlavor selectedFlavor = selectTextFlavor(flavors);

            if (selectedFlavor == null) {
                // No compatible flavor - should never happen
                return false;
            }

            logger.info("Selected flavor is {}", selectedFlavor.getHumanPresentableName());

            // Get the transferable data
            try (BufferedReader data = new BufferedReader(selectedFlavor.getReaderForText(transferable))) {
                data.lines().forEach(messageView::view);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private static DataFlavor selectTextFlavor(DataFlavor[] flavors) {
        // Look for either plain text or a String.
        for (DataFlavor flavor : flavors) {
            logger.info("Drop MIME type {} is available", flavor.getMimeType());
            if (flavor.equals(DataFlavor.stringFlavor)) {
                return flavor;
            }
        }
        return null;
    }

}
