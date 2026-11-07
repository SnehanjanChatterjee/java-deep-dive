import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Verifies cleanup for interruption before and after thread startup. @author Snehanjan Chatterjee */
public final class CooperativeStopLessonTest {
    @Test public void testSignalAlreadySetWhenWorkBegins() throws InterruptedException {
        final CooperativeStopLesson.Worker task = new CooperativeStopLesson.Worker();
        final Thread worker = new Thread(() -> { Thread.currentThread().interrupt(); task.run(); });
        worker.setDaemon(true); worker.start(); worker.join(5000);
        assertFalse(worker.isAlive()); assertTrue(task.cleaned());
    }
    @Test public void testCancellationOwnsWorkerThroughCleanup() throws InterruptedException {
        final CooperativeStopLesson.Worker task = new CooperativeStopLesson.Worker();
        final Thread worker = new Thread(task); worker.setDaemon(true); worker.start();
        try {
            worker.interrupt(); worker.join(5000);
            assertFalse(worker.isAlive()); assertTrue(task.cleaned());
        } finally { worker.interrupt(); worker.join(5000); }
    }
}
