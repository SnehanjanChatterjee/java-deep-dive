import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/** Verifies a checked contract keeps the original parsing failure. @author Snehanjan Chatterjee */
public final class CheckedFailureLessonTest {
    @Test public void testValidCountIncludingZero() throws CheckedFailureLesson.CountFormatException {
        assertEquals(0, CheckedFailureLesson.readCount("0"));
        assertEquals(12, CheckedFailureLesson.readCount("12"));
    }
    @Test public void testInvalidInputsPreserveCauses() {
        final CheckedFailureLesson.CountFormatException malformed = assertThrows(
                CheckedFailureLesson.CountFormatException.class, () -> CheckedFailureLesson.readCount("many"));
        assertTrue(malformed.getCause() instanceof NumberFormatException);
        assertThrows(CheckedFailureLesson.CountFormatException.class, () -> CheckedFailureLesson.readCount(null));
        final CheckedFailureLesson.CountFormatException negative = assertThrows(
                CheckedFailureLesson.CountFormatException.class, () -> CheckedFailureLesson.readCount("-1"));
        assertEquals("negative count", negative.getCause().getMessage());
    }
}
