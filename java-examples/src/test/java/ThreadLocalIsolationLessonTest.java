import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks one key does not mean one global value and repeated boundaries remain clean. @author Snehanjan Chatterjee */
public final class ThreadLocalIsolationLessonTest {
    @Test public void testIndependentAssociationsAndRepeatedCleanup() throws InterruptedException {
        assertArrayEquals(new int[] {0, 1, 7}, ThreadLocalIsolationLesson.observations());
        assertArrayEquals(new int[] {0, 1, 7}, ThreadLocalIsolationLesson.observations());
    }
}
