/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.utilities.StringUtils;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JEditorPane;
import org.apache.log4j.Logger;

/**
 *
 * @author martin
 */
public class EditorDropTarget implements DropTargetListener
{
    private static final Logger logger = Logger.getLogger(EditorDropTarget.class.getName());
    private final JEditorPane pane;
    private final DropTarget dropTarget;
    private boolean acceptableType;
    private boolean draggingFile;
    private Color feedbackColor;
    private final Color backgroundColor;
    private boolean changingBackground;

    LoadMessageInterface main_win;

    public EditorDropTarget( LoadMessageInterface main_win, JEditorPane pane )
    {
        this.pane = pane;
        this.main_win = main_win;

        feedbackColor = backgroundColor = pane.getBackground();

       // Create the DropTarget and register
       // it with the JEditorPane.
       dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
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
        
          // Check the drop action
    if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
      // Accept the drop and get the transfer data
      dtde.acceptDrop(dtde.getDropAction());
      Transferable transferable = dtde.getTransferable();

      try {
        boolean result = false;

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

  protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
    int dropAction = dtde.getDropAction();
    int sourceActions = dtde.getSourceActions();
    boolean acceptedDrag = false;

     logger.info("\tSource actions are "
        + sourceActions + ", drop action is "
        + dropAction);

    // Reject if the object being transferred
    // or the operations available are not acceptable.
    if (!acceptableType
        || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
       logger.info("Drop target rejecting drag");
      dtde.rejectDrag();
    } else if (!draggingFile && (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
      // Can't drag text to a read-only JEditorPane
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

  protected void dragUnderFeedback(DropTargetDragEvent dtde,
      boolean acceptedDrag) {
    if (draggingFile) {
      // When dragging a file, change the background color
      Color newColor = (dtde != null && acceptedDrag ? feedbackColor
          : backgroundColor);
      if (newColor.equals(pane.getBackground()) == false) {
        changingBackground = true;
        pane.setBackground(newColor);
        changingBackground = false;
        pane.repaint();
      }
    } else {
      if (dtde != null && acceptedDrag) {
        // Dragging text - move the insertion cursor
        Point location = dtde.getLocation();
        pane.getCaret().setVisible(true);
        pane.setCaretPosition(pane.viewToModel(location));
      } else {
        pane.getCaret().setVisible(false);
      }
    }
  }

  protected void checkTransferType(DropTargetDragEvent dtde) {
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
  protected boolean dropFile(Transferable transferable) throws IOException,
      UnsupportedFlavorException, MalformedURLException {
    List fileList = (List) transferable
        .getTransferData(DataFlavor.javaFileListFlavor);
    File transferFile = (File) fileList.get(0);
    final URL transferURL = transferFile.toURL();
    logger.info("File URL is " + transferURL);

    //pane.setPage(transferURL);
    main_win.loadMessage( transferURL.getFile() );

    return true;
  }

  // This method handles a drop with data content
  protected boolean dropContent(Transferable transferable,
      DropTargetDropEvent dtde) {

    try {
      // Check for a match with the current content type
      DataFlavor[] flavors = dtde.getCurrentDataFlavors();

      DataFlavor selectedFlavor = null;

      // Look for either plain text or a String.
      for (int i = 0; i < flavors.length; i++) {
        DataFlavor flavor = flavors[i];
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
          /*
        int selectionStart = pane.getCaretPosition();
        pane.replaceSelection(insertData);
        pane.select(selectionStart, selectionStart
            + insertData.length());
           */

           String files_to_open[] = insertData.split("\n");

           for( String  file_to_open : files_to_open )
                main_win.loadMessage( file_to_open );

        return true;
      }
      return false;
    } catch (Exception e) {
        logger.error(StringUtils.exceptionToString(e));
        return false;
    }
  }

}
