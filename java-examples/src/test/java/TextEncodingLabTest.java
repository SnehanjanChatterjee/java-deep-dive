import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks valid complete text, empty text and malformed encodings. @author Snehanjan Chatterjee */
public final class TextEncodingLabTest {
    @Test public void testCompleteTextAndEmpty() throws CharacterCodingException {
        assertEquals("Java \u20ac", TextEncodingLab.decodeStrict("Java \u20ac".getBytes(StandardCharsets.UTF_8)));
        assertEquals("", TextEncodingLab.decodeStrict(new byte[0]));
    }
    @Test public void testMalformedIncompleteAndNull() {
        assertThrows(CharacterCodingException.class, () -> TextEncodingLab.decodeStrict(new byte[] {(byte) 0xE2}));
        assertThrows(CharacterCodingException.class, () -> TextEncodingLab.decodeStrict(new byte[] {(byte) 0xC3, 0x28}));
        assertThrows(NullPointerException.class, () -> TextEncodingLab.decodeStrict(null));
    }
}
