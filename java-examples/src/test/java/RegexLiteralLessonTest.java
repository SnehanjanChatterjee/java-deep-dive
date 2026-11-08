import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks two quoting layers, missing matches and null policies. */
public final class RegexLiteralLessonTest {
    @Test public void testLiteralNeedleAndReplacement() {
        assertEquals("a$1b$1a", RegexLiteralLesson.replaceLiteral("a.b.a", ".", "$1"));
        assertEquals("x\\y", RegexLiteralLesson.replaceLiteral("x.y", ".", "\\"));
        assertEquals("a-b", RegexLiteralLesson.replaceLiteral("a[0]b", "[0]", "-"));
        assertEquals("abc", RegexLiteralLesson.replaceLiteral("abc", "?", "!"));
        assertEquals("", RegexLiteralLesson.replaceLiteral("", "?", "!"));
        assertEquals("-a-b-", RegexLiteralLesson.replaceLiteral("ab", "", "-"));
    }
    @Test public void testNullPolicies() {
        assertThrows(NullPointerException.class, () -> RegexLiteralLesson.replaceLiteral(null, ".", "x"));
        assertThrows(NullPointerException.class, () -> RegexLiteralLesson.replaceLiteral("a", null, "x"));
        assertThrows(NullPointerException.class, () -> RegexLiteralLesson.replaceLiteral("a", ".", null));
    }
}
