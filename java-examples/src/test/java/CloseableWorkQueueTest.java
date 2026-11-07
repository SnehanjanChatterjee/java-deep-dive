import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Verifies queue storage and terminal wait predicates. @author Snehanjan Chatterjee */
public final class CloseableWorkQueueTest {
    @Test public void testWraparoundDrainAndClosure() throws InterruptedException {
        final CloseableWorkQueue<String> queue = new CloseableWorkQueue<>(2);
        queue.put("A"); queue.put("B"); assertEquals("A", queue.take());
        queue.put("C"); queue.close(); queue.close();
        assertEquals("B", queue.take()); assertEquals("C", queue.take()); assertNull(queue.take());
        assertThrows(IllegalStateException.class, () -> queue.put("D"));
    }
    @Test public void testInvalidCapacityAndNull() {
        assertThrows(IllegalArgumentException.class, () -> new CloseableWorkQueue<>(0));
        final CloseableWorkQueue<String> queue = new CloseableWorkQueue<>(1);
        assertThrows(NullPointerException.class, () -> queue.put(null));
    }
    @Test public void testCloseReleasesWaitingConsumer() throws InterruptedException {
        final CloseableWorkQueue<String> queue = new CloseableWorkQueue<>(1);
        final CountDownLatch started = new CountDownLatch(1);
        final AtomicReference<Object> outcome = new AtomicReference<>("pending");
        final Thread consumer = new Thread(() -> {
            started.countDown();
            try { outcome.set(queue.take()); }
            catch (final InterruptedException failure) { Thread.currentThread().interrupt(); outcome.set(failure); }
        });
        consumer.start();
        try { assertTrue(started.await(5, TimeUnit.SECONDS)); queue.close(); consumer.join(5000); assertTrue(!consumer.isAlive()); assertNull(outcome.get()); }
        finally { queue.close(); consumer.interrupt(); consumer.join(5000); }
    }
    @Test public void testCloseRejectsWaitingProducer() throws InterruptedException {
        final CloseableWorkQueue<String> queue = new CloseableWorkQueue<>(1); queue.put("first");
        final CountDownLatch started = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        final Thread producer = new Thread(() -> {
            started.countDown();
            try { queue.put("second"); }
            catch (final IllegalStateException closed) { failure.set(closed); }
            catch (final InterruptedException interrupted) { Thread.currentThread().interrupt(); failure.set(interrupted); }
        });
        producer.start();
        try { assertTrue(started.await(5, TimeUnit.SECONDS)); queue.close(); producer.join(5000); assertTrue(!producer.isAlive()); assertTrue(failure.get() instanceof IllegalStateException); assertEquals("first", queue.take()); assertNull(queue.take()); }
        finally { queue.close(); producer.interrupt(); producer.join(5000); }
    }
    @Test public void testInterruptedTakePreservesStorage() throws InterruptedException {
        final CloseableWorkQueue<Integer> queue = new CloseableWorkQueue<>(1);
        Thread.currentThread().interrupt();
        try { assertThrows(InterruptedException.class, queue::take); }
        finally { Thread.interrupted(); }
        queue.put(7); assertEquals(Integer.valueOf(7), queue.take()); queue.close();
    }
}
