import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Verifies actual membership without relying on estimated group counts. @author Snehanjan Chatterjee */
public final class LegacyThreadGroupLessonTest {
    @Test public void testExplicitlyOwnedWorkersShareGroup() throws InterruptedException {
        assertArrayEquals(new String[] {"lesson-group", "lesson-group"}, LegacyThreadGroupLesson.memberships());
    }
}
