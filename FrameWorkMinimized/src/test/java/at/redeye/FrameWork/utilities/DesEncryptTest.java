package at.redeye.FrameWork.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DesEncryptTest {

    @Test
    void should_decode_encoded_text() throws Exception {
        final String passwd = "AberHallo";
        final String text = "Ein Hund kam in die Küche";

        DesEncrypt en_cipher = new DesEncrypt(passwd);
        DesEncrypt de_cipher = new DesEncrypt(passwd);

        String encoded = en_cipher.encrypt(text);

        assertEquals("Ein Hund kam in die Küche", de_cipher.decrypt(encoded));
    }
}