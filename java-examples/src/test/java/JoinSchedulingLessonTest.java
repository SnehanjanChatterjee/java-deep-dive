import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Verifies that an early join has not executed or started its target. @author Snehanjan Chatterjee */
public final class JoinSchedulingLessonTest {
    @Test public void testEarlyJoinAndStartedJoinHaveDifferentEvidence() throws InterruptedException {
        assertArrayEquals(new String[] {"after early join=NEW, answer=0",
            "after started join=TERMINATED, answer=42"}, JoinSchedulingLesson.observations());
    }
}
