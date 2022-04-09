package at.redeye.FrameWork.utilities;

import org.apache.commons.io.file.DeletingPathVisitor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteDir {

    static public void deleteDirectory(Path path) {
        try {
            Files.walkFileTree(path, DeletingPathVisitor.withLongCounters());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
