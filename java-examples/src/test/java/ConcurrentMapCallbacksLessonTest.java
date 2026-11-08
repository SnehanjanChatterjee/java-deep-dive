import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks absent/null/failure branches do not install a mapping. @author Snehanjan Chatterjee */
public final class ConcurrentMapCallbacksLessonTest {
    @Test public void testAbsentAttemptsCanRepeatUntilAValueIsInstalled() {
        assertArrayEquals(new String[] {"absent after null=true", "failure=calculation failed", "absent after failure=true",
            "installed=42", "existing=42", "callback calls=3"}, ConcurrentMapCallbacksLesson.observations());
    }
}
