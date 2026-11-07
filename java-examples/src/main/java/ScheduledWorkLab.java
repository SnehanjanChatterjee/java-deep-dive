import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/** Demonstrates periodic task outcomes without claiming exact wall-clock scheduling. @author Snehanjan Chatterjee */
public final class ScheduledWorkLab {
    /** Observes at least three tick signals, then cancels the periodic result. */
    static boolean awaitTicksAndCancel() throws InterruptedException {
        final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.setRemoveOnCancelPolicy(true);
        final CountDownLatch ticks = new CountDownLatch(3);
        try {
            final ScheduledFuture<?> periodic = scheduler.scheduleAtFixedRate(ticks::countDown, 0, 1, TimeUnit.MILLISECONDS);
            if (!ticks.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("three ticks did not complete"); }
            return periodic.cancel(false) && periodic.isCancelled();
        } finally {
            scheduler.shutdownNow();
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) { throw new IllegalStateException("scheduler did not terminate"); }
        }
    }

    /** Retrieves an exceptional periodic outcome; later executions are suppressed by contract. */
    static int periodicFailureRuns() throws InterruptedException, TimeoutException {
        final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.setRemoveOnCancelPolicy(true);
        final AtomicInteger runs = new AtomicInteger();
        try {
            final ScheduledFuture<?> periodic = scheduler.scheduleAtFixedRate(() -> {
                runs.incrementAndGet();
                throw new IllegalStateException("periodic failure");
            }, 0, 1, TimeUnit.MILLISECONDS);
            try {
                periodic.get(5, TimeUnit.SECONDS);
                throw new IllegalStateException("expected periodic failure");
            } catch (final ExecutionException failure) {
                if (!(failure.getCause() instanceof IllegalStateException)) {
                    throw new IllegalStateException("unexpected task failure", failure.getCause());
                }
                return runs.get();
            }
        } finally {
            scheduler.shutdownNow();
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) { throw new IllegalStateException("scheduler did not terminate"); }
        }
    }

    /** Prints outcome guarantees rather than timing measurements. */
    public static void main(final String[] args) throws InterruptedException, TimeoutException {
        System.out.println("cancelled=" + awaitTicksAndCancel());
        System.out.println("failed runs=" + periodicFailureRuns());
    }
}
