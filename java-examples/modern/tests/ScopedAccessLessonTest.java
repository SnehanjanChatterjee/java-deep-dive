import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks absent versus present-null context and automatic exit restoration. @author Snehanjan Chatterjee */
public final class ScopedAccessLessonTest {
    @Test public void testAbsentAndPresentNullAreDifferentStates() {
        assertArrayEquals(new String[] {"unbound fallback=guest", "unbound get=NoSuchElementException",
            "inside bound=true", "inside fallback=null", "after bound=false"}, ScopedAccessLesson.observations());
    }
}
