import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks control-flow effects rather than only exception messages. */
public final class ExceptionPathsLessonTest {
    @Test public void testNormalAndHandledPaths() {
        assertEquals("try body-end finally after", ExceptionPathsLesson.path(false));
        assertEquals("try catch finally after", ExceptionPathsLesson.path(true));
    }
    @Test public void testReturnValueIsEvaluatedBeforeFinally() {
        assertEquals(1, ExceptionPathsLesson.returnBeforeFinally());
    }
}
