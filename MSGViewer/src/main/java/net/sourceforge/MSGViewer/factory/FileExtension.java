package net.sourceforge.MSGViewer.factory;

import java.io.File;

public class FileExtension {

    private final String filename;

    public FileExtension(File file) {
        this.filename = file.getName();
    }

    public FileExtension(String filename) {
        this.filename = filename;
    }

    public String toString() {
        int idx = filename.lastIndexOf('.');
        return filename.substring(idx + 1).toLowerCase();
    }

}
