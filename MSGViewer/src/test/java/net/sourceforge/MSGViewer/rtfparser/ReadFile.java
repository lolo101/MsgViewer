package net.sourceforge.MSGViewer.rtfparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This program reads a text file line by line and print to the console. It uses
 * FileOutputStream to read the file.
 */
public class ReadFile {

    public static String read_file(String file_name) {
        try {
            return Files.readString(Path.of(file_name));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}