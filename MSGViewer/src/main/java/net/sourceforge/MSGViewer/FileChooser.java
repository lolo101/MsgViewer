package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Setup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChooser {
    private static final Logger logger = LogManager.getLogger(FileChooser.class);
    private static final String LAST_PATH_KEY = "LastPath";
    private final BaseWin parent;
    private final Setup setup;

    public FileChooser(BaseWin parent) {
        this.parent = parent;
        setup = parent.root.getSetup();
    }

    public Stream<File> chooseFilesToOpen() {
        JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new MSGFileFilter(parent.root));
        fc.setMultiSelectionEnabled(true);

        String lastPath = setup.getConfig(LAST_PATH_KEY, null);
        logger.info("last path: {}", lastPath);
        if (lastPath != null) {
            fc.setCurrentDirectory(new File(lastPath));
        }
        int retval = fc.showOpenDialog(parent);
        if (retval != JFileChooser.APPROVE_OPTION) {
            return Stream.empty();
        }
        return Arrays.stream(fc.getSelectedFiles());
    }

    public Optional<File> chooseFilesToSave() {
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        final FileFilter msg_filter = new FileNameExtensionFilter(parent.MlM("Outlook *.msg File"), "msg");
        final FileFilter mbox_filter = new FileNameExtensionFilter(parent.MlM("Unix *.mbox File"), "mbox");
        final FileFilter eml_filter = new FileNameExtensionFilter(parent.MlM("Thunderbird *.eml File"), "eml");
        fc.addChoosableFileFilter(msg_filter);
        fc.addChoosableFileFilter(mbox_filter);
        fc.addChoosableFileFilter(eml_filter);
        fc.setMultiSelectionEnabled(false);
        String lastPath = setup.getConfig(LAST_PATH_KEY, null);
        logger.info("last path: {}", lastPath);
        if (lastPath != null) {
            fc.setCurrentDirectory(new File(lastPath));
        }
        int retval = fc.showSaveDialog(parent);
        if (retval != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }
        File file = getSelectedFileWithExtension(fc);
        setup.setLocalConfig(LAST_PATH_KEY, file.getParent());
        return Optional.of(file);
    }

    private static File getSelectedFileWithExtension(JFileChooser fc) {
        File file = fc.getSelectedFile();
        FileNameExtensionFilter fileFilter = (FileNameExtensionFilter) fc.getFileFilter();
        return hasExtension(file) ? file : new File(file.getAbsolutePath() + "." + fileFilter.getExtensions()[0]);
    }

    private static boolean hasExtension(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".msg")
                || fileName.endsWith(".eml")
                || fileName.endsWith(".mbox");
    }
}
