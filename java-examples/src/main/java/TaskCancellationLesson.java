import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** Separates Future cancellation from completed task cleanup. @author Snehanjan Chatterjee */
public final class TaskCancellationLesson {
    /** Cancels running interruptible work and waits for its finally block independently. */
    public static void main(final String[] args) throws Exception {
        final CountDownLatch started = new CountDownLatch(1);
        final CountDownLatch cleanup = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Future<Integer> result = executor.submit(() -> {
                started.countDown();
                try { release.await(); return 42; }
                finally { cleanup.countDown(); }
            });
            if (!started.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("task did not start"); }
            System.out.println(result.cancel(true));
            try { result.get(); }
            catch (final CancellationException expected) { System.out.println("cancelled result"); }
            if (!cleanup.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("cleanup did not finish"); }
            System.out.println("cleanup finished");
        } finally { release.countDown(); executor.shutdownNow(); }
    }
}
