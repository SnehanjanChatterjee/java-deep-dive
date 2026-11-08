import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks which successfully initialized resource closes and which failure stays primary. */
public final class ResourceInitializationLessonTest {
    @Test public void testOpeningFailureClosesOnlyFirstResource() {
        assertEquals("open A\nopen B\nclose A\nprimary=open B\n", ResourceInitializationLesson.trace(false));
    }
    @Test public void testOpeningFailureRetainsSuppressedClose() {
        assertEquals("open A\nopen B\nclose A\nprimary=open B\nsuppressed=close A\n",
                ResourceInitializationLesson.trace(true));
    }
}
