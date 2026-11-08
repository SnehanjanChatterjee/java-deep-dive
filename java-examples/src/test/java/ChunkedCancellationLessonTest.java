import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks completed-chunk accounting and cancellation checkpoints. @author Snehanjan Chatterjee */
public final class ChunkedCancellationLessonTest {
    private static ChunkedCancellationLesson.Worker runWorker(final int requested, final int cancelAfter)
        throws InterruptedException {
        final ChunkedCancellationLesson.Worker task = new ChunkedCancellationLesson.Worker(requested, cancelAfter);
        final Thread thread = new Thread(task, "test-chunk-worker");
        thread.start();
        thread.join();
        return task;
    }

    @Test public void testCancellationPreservesOnlyCompleteCommits() throws InterruptedException {
        final ChunkedCancellationLesson.Worker task = runWorker(5, 3);
        assertEquals(3, task.completed());
        assertEquals(15150, task.checksum());
        assertTrue(task.cleaned());
    }

    @Test public void testEmptyAndEntryCancellationStillCleanUp() throws InterruptedException {
        final ChunkedCancellationLesson.Worker empty = runWorker(0, -1);
        assertEquals(0, empty.completed());
        assertEquals(0, empty.checksum());
        assertTrue(empty.cleaned());
        final ChunkedCancellationLesson.Worker cancelled = runWorker(5, 0);
        assertEquals(0, cancelled.completed());
        assertEquals(0, cancelled.checksum());
        assertTrue(cancelled.cleaned());
    }

    @Test public void testNoRequestOrLaterRequestAllowsFullCompletion() throws InterruptedException {
        final ChunkedCancellationLesson.Worker normal = runWorker(2, -1);
        assertEquals(2, normal.completed());
        assertEquals(10100, normal.checksum());
        assertTrue(normal.cleaned());
        assertEquals(2, runWorker(2, 3).completed());
    }

    @Test public void testInvalidConfigurationDoesNotStartWork() {
        assertThrows(IllegalArgumentException.class, () -> new ChunkedCancellationLesson.Worker(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new ChunkedCancellationLesson.Worker(1, -2));
    }
}
