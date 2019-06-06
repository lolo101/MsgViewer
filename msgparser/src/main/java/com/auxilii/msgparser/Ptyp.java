package com.auxilii.msgparser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

enum Ptyp {
    PtypInteger16(0x0002, false, s -> (short) s.readLong()),
    PtypInteger32(0x0003, false, s -> (int) s.readLong()),
    PtypFloating32(0x0004, false, s -> Float.intBitsToFloat((int) s.readLong())),
    PtypFloating64(0x0005, false, DocumentInputStream::readDouble),
    PtypCurrency(0x0006, false, s -> BigDecimal.valueOf(s.readLong(), 4)),
    PtypFloatingTime(0x0007, false, Ptyp::toFloatingTime),
    PtypErrorCode(0x000a, false, s -> (int) s.readLong()),
    PtypBoolean(0x000b, false, s -> s.readLong() == 1),
    PtypInteger64(0x0014, false, DocumentInputStream::readLong),
    PtypTime(0x0040, false, Ptyp::toTime),

    PtypString8(0x001e, true, s -> new String(getBytes(s), StandardCharsets.ISO_8859_1)),
    PtypString(0x001f, true, s -> new String(getBytes(s), StandardCharsets.UTF_16LE)),
    PtypGuid(0x0048, true, Ptyp::getBytes),
    PtypBinary(0x0102, true, Ptyp::getBytes),

    PtypMultipleInteger16(0x1002, false, DocumentInputStream::readShort),
    PtypMultipleInteger32(0x1003, false, DocumentInputStream::readInt),
    PtypMultipleFloating32(0x1004, false, s -> Float.intBitsToFloat(s.readInt())),
    PtypMultipleFloating64(0x1005, false, DocumentInputStream::readDouble),
    PtypMultipleCurrency(0x1006, false, s -> BigDecimal.valueOf(s.readLong(), 4)),
    PtypMultipleFloatingTime(0x1007, false, Ptyp::toFloatingTime),
    PtypMultipleTime(0x1040, false, Ptyp::toTime),
    PtypMultipleGuid(0x1048, false, s -> getBytes(s, 16)),
    PtypMultipleInteger64(0x1014, false, DocumentInputStream::readLong),

    PtypMultipleBinary(0x1102, true, Ptyp::toBinaryLengths),
    PtypMultipleString8(0x101e, true, Ptyp::toStringLengths),
    PtypMultipleString(0x101f, true, Ptyp::toStringLengths);

    private static final String SUBENTRY_PREFIX = "__substg1.0_";
    private static final int MULTIPLE_VALUED_FLAG = 0x1000;
    private static final LocalDateTime FLOATING_TIME_EPOCH = LocalDateTime.of(1899, Month.DECEMBER, 30, 0, 0);
    private static final LocalDateTime TIME_EPOCH = LocalDateTime.of(1601, Month.JANUARY, 1, 0, 0);

    private final int type;
    private final boolean variableLength;
    private final Function<DocumentInputStream, ?> conversion;

    private Ptyp(int type, boolean variableLength, Function<DocumentInputStream, ?> conversion) {
        this.type = type;
        this.variableLength = variableLength;
        this.conversion = conversion;
    }

    public static Ptyp from(int type) {
        for (Ptyp value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown type: %04x", type));
    }

    public Object parseValue(DocumentInputStream propertyStream, DirectoryEntry dir, String pTag) throws IOException {
        if (variableLength || isMultipleValued()) {
            int byteCount = propertyStream.readInt();
            int reserved = propertyStream.readInt();
            return parseSubStream(dir, pTag);
        }
        return convert(propertyStream);
    }

    private Object parseSubStream(DirectoryEntry dir, String pTag) throws IOException {
        DocumentEntry subEntry = (DocumentEntry) dir.getEntry(SUBENTRY_PREFIX + pTag);
        try (DocumentInputStream subStream = new DocumentInputStream(subEntry)) {
            if (isMultipleValued()) {
                if (variableLength) {
                    return parseMultipleVariableLengthValues(subStream, dir, pTag);
                }
                return parseMultipleFixedLengthValues(subStream);
            }
            return convert(subStream);
        }
    }

    private Object parseMultipleVariableLengthValues(final DocumentInputStream subStream, DirectoryEntry dir, String pTag) throws IOException {
        int[] lengths = (int[]) convert(subStream);
        Object[] pValues = new Object[lengths.length];
        for (int index = 0; index < lengths.length; index++) {
            DocumentEntry valueEntry = (DocumentEntry) dir.getEntry(String.format("%s%s-%08X", SUBENTRY_PREFIX, pTag, index));
            try (DocumentInputStream valueStream = new DocumentInputStream(valueEntry)) {
                pValues[index] = convert(valueStream);
            }
        }
        return pValues;
    }

    private Object parseMultipleFixedLengthValues(DocumentInputStream subStream) {
        List<Object> pValues = new ArrayList<>();
        while (subStream.available() > 0) {
            pValues.add(convert(subStream));
        }
        return pValues.toArray();
    }

    private boolean isMultipleValued() {
        return (type & MULTIPLE_VALUED_FLAG) == MULTIPLE_VALUED_FLAG;
    }

    private Object convert(DocumentInputStream value) {
        return conversion.apply(value);
    }

    private static byte[] getBytes(DocumentInputStream s) {
        try {
            return s.readAllBytes();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static byte[] getBytes(DocumentInputStream s, int len) {
        try {
            return s.readNBytes(len);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static LocalDateTime toFloatingTime(DocumentInputStream s) {
        double x = s.readDouble();
        long days = (long) x;
        double decimal = x - days;
        long nanos = (long) (decimal * 24 * 60 * 60 * 1_000_000_000);
        return FLOATING_TIME_EPOCH.plusDays(days).plusNanos(nanos);
    }

    private static LocalDateTime toTime(DocumentInputStream x) {
        return TIME_EPOCH.plus(x.readLong() / 10, ChronoUnit.MICROS);
    }

    private static int[] toBinaryLengths(DocumentInputStream subStream) {
        int[] lengths = new int[subStream.available() / 8];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = subStream.readInt();
            int reserved = subStream.readInt();
        }
        return lengths;
    }

    private static int[] toStringLengths(DocumentInputStream subStream) {
        int[] lengths = new int[subStream.available()/ 4];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = subStream.readInt();
        }
        return lengths;
    }
}
