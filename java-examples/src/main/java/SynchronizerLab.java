import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/** Separates completion signaling, failure propagation and resource permits. @author Snehanjan Chatterjee */
public final class SynchronizerLab {
    /** Publishes two independent result slots through latch completion. */
    public static int sum() throws InterruptedException {
        final int[] results = new int[2];
        final CountDownLatch completed = new CountDownLatch(results.length);
        final Semaphore resource = new Semaphore(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        final Thread[] workers = new Thread[results.length];
        for (int index = 0; index < results.length; index++) {
            final int slot = index;
            workers[index] = new Thread(() -> {
                try {
                    resource.acquire();
                    try { results[slot] = slot + 20; }
                    finally { resource.release(); }
                } catch (final InterruptedException interrupted) {
                    Thread.currentThread().interrupt(); failure.compareAndSet(null, interrupted);
                } finally { completed.countDown(); }
            });
            workers[index].start();
        }
        try {
            if (!completed.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("workers timed out"); }
            if (failure.get() != null) { throw new IllegalStateException("worker failed", failure.get()); }
            return results[0] + results[1];
        } finally {
            for (final Thread worker : workers) { worker.interrupt(); }
            for (final Thread worker : workers) { worker.join(); }
        }
    }
    /** Prints a result only after completion and failure checks. */
    public static void main(final String[] args) throws InterruptedException { System.out.println(sum()); }
}
