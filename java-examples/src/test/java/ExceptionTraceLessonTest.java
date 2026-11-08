import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks frame filtering without relying on release-specific library frame positions. */
public final class ExceptionTraceLessonTest {
    @Test public void testApplicationFrameOrderAndExcludedLibraryFrame() {
        final IllegalArgumentException failure = new IllegalArgumentException("example");
        failure.setStackTrace(new StackTraceElement[] {
            new StackTraceElement("java.lang.Integer", "parseInt", "Integer.java", 1),
            new StackTraceElement("ExceptionTraceLesson", "parseCount", "ExceptionTraceLesson.java", 2),
            new StackTraceElement("ExceptionTraceLesson", "loadCount", "ExceptionTraceLesson.java", 3)
        });
        assertEquals("IllegalArgumentException\nparseCount\nloadCount", ExceptionTraceLesson.applicationFrames(failure));
    }
    @Test public void testEmptyAndNullTraceInputs() {
        final IllegalArgumentException failure = new IllegalArgumentException("example");
        failure.setStackTrace(new StackTraceElement[0]);
        assertEquals("IllegalArgumentException", ExceptionTraceLesson.applicationFrames(failure));
        assertThrows(NullPointerException.class, () -> ExceptionTraceLesson.applicationFrames(null));
    }
}
