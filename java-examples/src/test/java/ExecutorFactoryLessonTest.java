import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks a service-owned finite batch and result publication. @author Snehanjan Chatterjee */
public final class ExecutorFactoryLessonTest {
    @Test(timeout = 10000) public void testDependentSubmissionDoesNotOccupyWorkerWhileWaiting() throws Exception {
        assertEquals(42, ExecutorFactoryLesson.runBatch());
    }
}
