package net.sourceforge.MSGViewer.factory;

import java.nio.file.Path;

public class FileExtension {

    private final String filename;

    public FileExtension(Path file) {
        this.filename = file.getFileName().toString();
    }

    public FileExtension(String filename) {
        this.filename = filename;
    }

    public String toString() {
        int idx = filename.lastIndexOf('.');
        return filename.substring(idx + 1).toLowerCase();
    }

}
