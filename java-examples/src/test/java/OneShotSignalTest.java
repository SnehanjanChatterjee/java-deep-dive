import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/** Covers one-way state, timeout, multiple acquirers and interruption. @author Snehanjan Chatterjee */
public final class OneShotSignalTest {
    @Test public void testOpenAndImmediateTimeout() throws InterruptedException {
        final OneShotSignal signal = new OneShotSignal();
        assertFalse(signal.await(0, TimeUnit.SECONDS));
        assertTrue(signal.open()); assertFalse(signal.open());
        assertTrue(signal.await(0, TimeUnit.SECONDS)); signal.await();
        assertThrows(IllegalArgumentException.class, () -> signal.await(-1, TimeUnit.SECONDS));
        assertThrows(NullPointerException.class, () -> signal.await(1, null));
    }
    @Test public void testSeveralSharedAcquirers() throws InterruptedException {
        final OneShotSignal signal = new OneShotSignal();
        final CountDownLatch finished = new CountDownLatch(2);
        final Runnable waiter = () -> { try { signal.await(); finished.countDown(); } catch (final InterruptedException interrupted) { Thread.currentThread().interrupt(); } };
        final Thread first = new Thread(waiter), second = new Thread(waiter);
        first.start(); second.start();
        try { signal.open(); assertTrue(finished.await(5, TimeUnit.SECONDS)); }
        finally { first.interrupt(); second.interrupt(); first.join(5000); second.join(5000); }
        assertFalse(first.isAlive()); assertFalse(second.isAlive());
    }
    @Test public void testInterruptibleWait() throws InterruptedException {
        final OneShotSignal signal = new OneShotSignal();
        final CountDownLatch ready = new CountDownLatch(1);
        final AtomicBoolean interrupted = new AtomicBoolean();
        final Thread worker = new Thread(() -> { ready.countDown(); try { signal.await(); } catch (final InterruptedException expected) { interrupted.set(true); Thread.currentThread().interrupt(); } });
        worker.start();
        try { assertTrue(ready.await(5, TimeUnit.SECONDS)); worker.interrupt(); worker.join(5000); assertFalse(worker.isAlive()); assertTrue(interrupted.get()); }
        finally { signal.open(); worker.interrupt(); worker.join(5000); }
    }
}
