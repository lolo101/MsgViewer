package com.auxilii.msgparser;

import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PtypTest {
    @Test
    void should_parse_MultipleVariableLengthValues() throws IOException {
        Ptyp sut = Ptyp.PtypMultipleString;
        String pTag = String.format("8029%04X", sut.id);

        byte[] expected0AsBytes = "expected-0".getBytes(StandardCharsets.UTF_16LE);
        byte[] expected1AsBytes = "expected-1".getBytes(StandardCharsets.UTF_16LE);
        POIFSFileSystem fileSystem = new POIFSFileSystem();

        InputStream subStorageStream = new ByteArrayInputStream(toByteArray(expected0AsBytes.length, expected1AsBytes.length));
        fileSystem.createDocument(subStorageStream, Ptyp.SUBSTORAGE_PREFIX + pTag);

        InputStream value0Stream = new ByteArrayInputStream(expected0AsBytes);
        fileSystem.createDocument(value0Stream, Ptyp.SUBSTORAGE_PREFIX + pTag + "-00000000");

        InputStream value1Stream = new ByteArrayInputStream(expected1AsBytes);
        fileSystem.createDocument(value1Stream, Ptyp.SUBSTORAGE_PREFIX + pTag + "-00000001");

        InputStream pstream = new ByteArrayInputStream(new byte[8]);
        DocumentEntry propertiesEntry = fileSystem.createDocument(pstream, "__properties_version1.0");
        try (DocumentInputStream propertiesStream = new DocumentInputStream(propertiesEntry)) {
            Object actual = sut.parseValue(propertiesStream, fileSystem.getRoot(), pTag);
            assertArrayEquals(new Object[]{"expected-0", "expected-1"}, (Object[]) actual);
        }
    }

    private static byte[] toByteArray(int... values) {
        byte[] bytes = new byte[values.length * 4];
        for (int i = 0; i < values.length; i++) {
            int value = values[i];
            bytes[4 * i] = (byte) (value >> 24);
            bytes[4 * i + 1] = (byte) (value >> 16);
            bytes[4 * i + 2] = (byte) (value >> 8);
            bytes[4 * i + 3] = (byte) (value);
        }
        return bytes;
    }
}