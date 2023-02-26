package at.redeye.FrameWork.base.bindtypes;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class DBStringTest {

    private static final int MAX_LEN = 10;
    private static final String NORMAL_STRING = "12345";
    private static final String LONG_STRING = "12345678901234567890";

    @Test
    void testGetDBType() {
        DBString instance = new DBString("name", MAX_LEN);
        assertEquals(DBDataType.DB_TYPE_STRING, instance.getDBType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "value of any length", ""})
    void testGetValue(String value) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromDB(value);
        assertEquals(value, instance.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "value of any length", ""})
    void testToString(String value) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromDB(value);
        assertEquals(value, instance.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "value", "that is loaded event when size is longer than max_len"})
    void testLoadFromString(String value) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromString(value);
        assertEquals(value, instance.value);
    }

    @ParameterizedTest
    @CsvSource({
            NORMAL_STRING + ", true",
            LONG_STRING + ", false"})
    void testAcceptString(String value, boolean accept) {
        DBString instance = new DBString("name", MAX_LEN);
        assertEquals(accept, instance.acceptString(value));
    }

    @Test
    void testGetCopy() {
        DBString instance = new DBString("name", MAX_LEN);
        instance.acceptString("value");
        DBString result = instance.getCopy();
        assertNotSame(result, instance);
        assertEquals(result.value, instance.value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "value", "that is loaded event when size is longer than max_len"})
    void testLoadFromCopy(String value) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromCopy(value);
        assertEquals(value, instance.value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "value", "that is loaded event when size is longer than max_len"})
    void testLoadFromDB(String value) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromDB(value);
        assertEquals(value, instance.value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 100, 256})
    void testGetMaxLen(int max_len) {
        DBString instance = new DBString("name", max_len);
        assertEquals(max_len, instance.getMaxLen());
    }

    @ParameterizedTest
    @CsvSource({
            "test, false",
            "'    ', false",
            "'', true"})
    void testIsEmpty(String value, boolean expResult) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromString(value);
        assertEquals(expResult, instance.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "test, false",
            "'    ', true",
            "'', true"})
    void testIsEmptyTrimmed(String value, boolean expResult) {
        DBString instance = new DBString("name", MAX_LEN);
        instance.loadFromString(value);
        assertEquals(expResult, instance.isEmptyTrimmed());
    }
}
