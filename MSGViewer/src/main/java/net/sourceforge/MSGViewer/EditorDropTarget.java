package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class EditorDropTarget implements DropTargetListener {
    private static final Logger logger = LogManager.getLogger(EditorDropTarget.class);
    private final LoadMessageInterface main_win;
    private final JEditorPane pane;
    private final Color feedbackColor;
    private final Color backgroundColor;
    private boolean acceptableType;
    private boolean draggingFile;

    public EditorDropTarget(LoadMessageInterface main_win, JEditorPane pane) {
        this.pane = pane;
        this.main_win = main_win;

        backgroundColor = pane.getBackground();
        feedbackColor = backgroundColor.darker();

        // Create the DropTarget and register
        // it with the JEditorPane.
        new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

        logger.debug("dragEnter");

        // Get the type of object being transferred and determine
        // whether it is appropriate.
        checkTransferType(dtde);

        // Accept or reject the drag.
        boolean acceptedDrag = acceptOrRejectDrag(dtde);

        // Do drag-under feedback
        dragUnderFeedback(dtde, acceptedDrag);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {


        logger.debug("dragOver");
        // Accept or reject the drag
        boolean acceptedDrag = acceptOrRejectDrag(dtde);

        // Do drag-under feedback
        dragUnderFeedback(dtde, acceptedDrag);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

        logger.debug("dropActionChanged");

        // Accept or reject the drag
        boolean acceptedDrag = acceptOrRejectDrag(dtde);

        // Do drag-under feedback
        dragUnderFeedback(dtde, acceptedDrag);
    }

    @Override
    public void dragExit(DropTargetEvent dte) {

        logger.debug("dragExit");
        // Do drag-under feedback
        dragUnderFeedback(null, false);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        logger.debug("drop");
        dragUnderFeedback(null, false);

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
                    result = dropContent(transferable, dtde);
                }

                dtde.dropComplete(result);
                logger.info("Drop completed, success: " + result);
            } catch (Exception e) {
                logger.info("Exception while handling drop " + e);
                dtde.rejectDrop();
            }
        } else {
            logger.info("Drop target rejected drop");
            dtde.dropComplete(false);
        }

    }
    // Internal methods start here

    private boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        boolean acceptedDrag = false;

        logger.info("\tSource actions are "
                + sourceActions + ", drop action is "
                + dropAction);

        // Reject if the object being transferred
        // or the operations available are not acceptable.
        if (!acceptableType || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            logger.info("Drop target rejecting drag");
            dtde.rejectDrag();
        } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            // Not offering copy or move - suggest a copy
            logger.info("Drop target offering COPY");
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            acceptedDrag = true;
        } else {
            // Offering an acceptable operation: accept
            logger.info("Drop target accepting drag");
            dtde.acceptDrag(dropAction);
            acceptedDrag = true;
        }

        return acceptedDrag;
    }

    private void dragUnderFeedback(DropTargetDragEvent dtde,
                                     boolean acceptedDrag) {
        boolean receptive = dtde != null && acceptedDrag;
        if (draggingFile) {
            // When dragging a file, change the background color
            Color newColor = receptive ? feedbackColor : backgroundColor;
            if (!newColor.equals(pane.getBackground())) {
                pane.setBackground(newColor);
                pane.repaint();
            }
        } else {
            if (receptive) {
                // Dragging text - move the insertion cursor
                Point location = dtde.getLocation();
                pane.getCaret().setVisible(true);
                pane.setCaretPosition(pane.viewToModel2D(location));
            } else {
                pane.getCaret().setVisible(false);
            }
        }
    }

    private void checkTransferType(DropTargetDragEvent dtde) {
        // Accept a list of files, or data content that
        // amounts to plain text or a Unicode text string
        acceptableType = false;
        draggingFile = false;
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            acceptableType = true;
            draggingFile = true;
        } else if (dtde.isDataFlavorSupported(DataFlavor.plainTextFlavor)
                || dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            acceptableType = true;
        }

        logger.info("File type acceptable - " + acceptableType);
        logger.info("Dragging a file - " + draggingFile);
    }

    // This method handles a drop for a list of files
    private boolean dropFile(Transferable transferable) throws IOException,
            UnsupportedFlavorException {
        List<File> fileList = (List<File>) transferable
                .getTransferData(DataFlavor.javaFileListFlavor);
        fileList.forEach(transferFile -> {
            logger.info("Opening file " + transferFile);
            main_win.loadMessage(transferFile.getPath());
        });

        return true;
    }

    // This method handles a drop with data content
    private boolean dropContent(Transferable transferable,
                                  DropTargetDropEvent dtde) {

        try {
            // Check for a match with the current content type
            DataFlavor[] flavors = dtde.getCurrentDataFlavors();

            DataFlavor selectedFlavor = null;

            // Look for either plain text or a String.
            for (DataFlavor flavor : flavors) {
                logger.info("Drop MIME type " + flavor.getMimeType()
                        + " is available");
                if (flavor.equals(DataFlavor.plainTextFlavor)
                        || flavor.equals(DataFlavor.stringFlavor)) {
                    selectedFlavor = flavor;
                    break;
                }
            }

            if (selectedFlavor == null) {
                // No compatible flavor - should never happen
                return false;
            }

            logger.info("Selected flavor is "
                    + selectedFlavor.getHumanPresentableName());

            // Get the transferable and then obtain the data
            Object data = transferable.getTransferData(selectedFlavor);

            logger.info("Transfer data type is "
                    + data.getClass().getName());

            String insertData = null;
            if (data instanceof InputStream) {
                // Plain text flavor
                String charSet = selectedFlavor.getParameter("charset");
                InputStream is = (InputStream) data;
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                try {
                    insertData = new String(bytes, charSet);
                } catch (UnsupportedEncodingException e) {
                    // Use the platform default encoding
                    insertData = new String(bytes);
                }
            } else if (data instanceof String) {
                // String flavor
                insertData = (String) data;
            }

            if (insertData != null) {

                logger.info("inserting text:" + insertData);

                String[] files_to_open = insertData.split("\n");

                for (String file_to_open : files_to_open)
                    main_win.loadMessage(file_to_open);

                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error(StringUtils.exceptionToString(e));
            return false;
        }
    }

}
