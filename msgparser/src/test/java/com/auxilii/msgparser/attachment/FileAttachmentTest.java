package com.auxilii.msgparser.attachment;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileAttachmentTest {

    @Nested
    class GetExtension {

        @Test
        void should_return_extension_based_on_longFilename_when_extension_missing() {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setLongFilename("toto.txt");
            assertEquals(".txt", fileAttachment.getExtension());
        }

        @Test
        void should_return_extension_based_on_filename_when_extension_missing() {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setFilename("toto.txt");
            assertEquals(".txt", fileAttachment.getExtension());
        }

        @Test
        void longFilename_should_have_precedence_over_filename() {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setFilename("toto.bat");
            fileAttachment.setLongFilename("toto.txt");
            assertEquals(".txt", fileAttachment.getExtension());
        }

        @Test
        void should_return_extension_if_available() {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setExtension(".txt");
            fileAttachment.setFilename("toto.bat");
            fileAttachment.setLongFilename("toto.exe");
            assertEquals(".txt", fileAttachment.getExtension());
        }

        @Test
        void should_return_empty_string_on_no_extention() {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setFilename("toto");
            fileAttachment.setLongFilename("long-toto");
            assertEquals("", fileAttachment.getExtension());
        }
    }
}
