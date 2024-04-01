package com.auxilii.msgparser;

import com.auxilii.msgparser.attachment.FileAttachment;
import com.google.common.jimfs.Jimfs;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MsgWriterTest {

    private static final String CONTENT_ID = "part1.HRgTI02d.mjRZp5Gh@neuf.fr";

    @Test
    void issue129() throws Exception {
        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path testOut = fileSystem.getPath("test_out.msg");
            try (OutputStream outputStream = Files.newOutputStream(testOut)) {
                Message msg = givenMessage();
                new MsgWriter(msg).write(outputStream);
            }

            try (POIFSFileSystem fs = new POIFSFileSystem(Files.newInputStream(testOut))) {
                DirectoryNode root = fs.getRoot();
                DirectoryNode attachment = (DirectoryNode) root.getEntry("__attach_version1.0_#00000000");
                DocumentEntry contentId = (DocumentEntry) attachment.getEntry("__substg1.0_3712001F");
                try (DocumentInputStream stream = new DocumentInputStream(contentId)) {
                    String contentIdValue = new String(stream.readAllBytes(), StandardCharsets.UTF_16LE);
                    assertThat(contentIdValue).isEqualTo(CONTENT_ID);
                }
            }
        }
    }

    private static Message givenMessage() {
        Message message = new Message();
        FileAttachment attachment = new FileAttachment();
        attachment.setContentId(CONTENT_ID);
        attachment.setData(new byte[0]);
        message.addAttachment(attachment);
        return message;
    }
}
