import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Checks restoration on normal, nested and exceptional boundaries. @author Snehanjan Chatterjee */
public final class ThreadContextLessonTest {
    @Test public void testNestedExceptionalRestoration() {
        assertNull(ThreadContextLesson.current());
        ThreadContextLesson.withContext("outer", () -> {
            assertThrows(IllegalStateException.class, () -> ThreadContextLesson.withContext("inner", () -> {
                assertEquals("inner", ThreadContextLesson.current()); throw new IllegalStateException("failure");
            }));
            assertEquals("outer", ThreadContextLesson.current());
        });
        assertNull(ThreadContextLesson.current());
    }
    @Test public void testInvalidArgumentsDoNotBind() {
        assertThrows(NullPointerException.class, () -> ThreadContextLesson.withContext(null, () -> { }));
        assertThrows(NullPointerException.class, () -> ThreadContextLesson.withContext("value", null));
        assertNull(ThreadContextLesson.current());
    }
}
