import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Uses the gate-established order rather than task-duration guesses. @author Snehanjan Chatterjee */
public final class CompletionOrderLessonTest {
    @Test(timeout = 10000) public void testIndependentCompletionIsRetrievedBeforeHeldSubmission() throws Exception {
        assertArrayEquals(new int[] {42, 1}, CompletionOrderLesson.collect());
    }
}
