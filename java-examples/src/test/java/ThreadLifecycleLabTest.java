import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Tests cooperative cancellation without sleep-based state guesses. @author Snehanjan Chatterjee */
public final class ThreadLifecycleLabTest {
    @Test public void testCancellationBeforeOrDuringWait() throws InterruptedException {
        final ThreadLifecycleLab.Worker task = new ThreadLifecycleLab.Worker();
        final Thread worker = new Thread(task); worker.setDaemon(true); worker.start();
        try { assertTrue(task.awaitStarted()); worker.interrupt(); worker.join(5000); assertFalse(worker.isAlive()); assertTrue(task.interruptedAtExit()); }
        finally { worker.interrupt(); worker.join(5000); }
    }
    @Test public void testInterruptAlreadySetAtEntry() throws InterruptedException {
        final ThreadLifecycleLab.Worker task = new ThreadLifecycleLab.Worker();
        final Thread worker = new Thread(() -> { Thread.currentThread().interrupt(); task.run(); });
        worker.setDaemon(true); worker.start(); worker.join(5000);
        assertFalse(worker.isAlive()); assertTrue(task.interruptedAtExit());
    }
}
