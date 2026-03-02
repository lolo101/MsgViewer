package com.auxilii.msgparser;

import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldInformationTest {

    @Test
    void should_detect_multipleVariableLengthValues() throws IOException {
        POIFSFileSystem fileSystem = new POIFSFileSystem();
        InputStream stream = new ByteArrayInputStream("expected".getBytes(StandardCharsets.UTF_16LE));
        DocumentEntry entry = fileSystem.createDocument(stream, Ptyp.SUBSTORAGE_PREFIX + "8029101F-00000000");
        FieldInformation sut = new FieldInformation(entry);
        assertEquals("expected", sut.getData());
    }
}