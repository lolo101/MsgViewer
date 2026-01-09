package com.auxilii.msgparser;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.jimfs.Jimfs;
import java.io.*;
import java.nio.file.*;
import org.apache.poi.poifs.filesystem.*;
import org.junit.jupiter.api.Test;

class MsgParserTest {
    @Test
    void should_ignore_trailing_bytes() throws IOException {
        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path messagePath = fileSystem.getPath("message.msg");
            writeMessageWithTrailingBytes(messagePath);
            MsgParser parser = new MsgParser(messagePath);
            assertDoesNotThrow(parser::parseMsg);
        }
    }

    private static void writeMessageWithTrailingBytes(Path target) throws IOException {
        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            byte[] content = {
                0,0,0,0,0,0,0,0, // reserved
                0,0,0,0, // nextRecipientId
                0,0,0,0, // nextAttachmentId
                0,0,0,0, // recipientCount
                0,0,0,0, // attachmentCount
                0,0,0,0,0,0,0,0, // reserved for top level message
                1,2,3,4 // trailing garbage
            };
            fs.getRoot().createDocument("__properties_version1.0", new ByteArrayInputStream(content));
            fs.writeFilesystem(Files.newOutputStream(target));
        }
    }
}
