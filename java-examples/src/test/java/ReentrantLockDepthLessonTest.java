import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks nested acquisition requires nested release. @author Snehanjan Chatterjee */
public final class ReentrantLockDepthLessonTest {
    @Test public void testHoldCountReturnsToZeroAfterBothFinallyBlocks() {
        assertArrayEquals(new int[] {1, 2, 1, 0}, ReentrantLockDepthLesson.depths());
    }
}
