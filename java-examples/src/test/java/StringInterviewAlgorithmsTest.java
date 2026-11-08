import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/** Checks ASCII, supplementary, empty and null behavior. @author Snehanjan Chatterjee */
public final class StringInterviewAlgorithmsTest {
    @Test public void testReverseCodePoints() {
        assertEquals("", StringInterviewAlgorithms.reverseCodePoints(""));
        assertEquals("cba", StringInterviewAlgorithms.reverseCodePoints("abc"));
        assertEquals("B\uD83D\uDE00A", StringInterviewAlgorithms.reverseCodePoints("A\uD83D\uDE00B"));
        assertThrows(NullPointerException.class, () -> StringInterviewAlgorithms.reverseCodePoints(null));
    }
    @Test public void testFirstUniqueCodePoint() {
        assertEquals('w', StringInterviewAlgorithms.firstUnique("swiss").getAsInt());
        assertEquals(0x1F600, StringInterviewAlgorithms.firstUnique("a\uD83D\uDE00a").getAsInt());
        assertFalse(StringInterviewAlgorithms.firstUnique("").isPresent());
        assertFalse(StringInterviewAlgorithms.firstUnique("aabb").isPresent());
        assertThrows(NullPointerException.class, () -> StringInterviewAlgorithms.firstUnique(null));
    }
    @Test public void testTrailingWhitespacePolicy() {
        assertEquals("", StringInterviewAlgorithms.stripTrailingWhitespace(""));
        assertEquals("", StringInterviewAlgorithms.stripTrailingWhitespace(" \t\n\u2003"));
        assertEquals("  Ada", StringInterviewAlgorithms.stripTrailingWhitespace("  Ada \t"));
        assertEquals("A\uD83D\uDE00", StringInterviewAlgorithms.stripTrailingWhitespace("A\uD83D\uDE00\u2003"));
        assertEquals("A\u00A0", StringInterviewAlgorithms.stripTrailingWhitespace("A\u00A0"));
        assertThrows(NullPointerException.class, () -> StringInterviewAlgorithms.stripTrailingWhitespace(null));
    }
}
