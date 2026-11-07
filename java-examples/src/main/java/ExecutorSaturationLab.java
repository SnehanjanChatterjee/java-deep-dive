import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** Coordinates pool saturation without timing-dependent sleeps. @author Snehanjan Chatterjee */
public final class ExecutorSaturationLab {
    /** Demonstrates core/queue/max admission and explicit rejection. */
    public static void main(final String[] args) throws InterruptedException {
        final CountDownLatch firstStarted = new CountDownLatch(1);
        final CountDownLatch bothStarted = new CountDownLatch(2);
        final CountDownLatch release = new CountDownLatch(1);
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1), new ThreadPoolExecutor.AbortPolicy());
        final Runnable blocked = () -> {
            firstStarted.countDown();
            bothStarted.countDown();
            try { release.await(); }
            catch (final InterruptedException interrupted) { Thread.currentThread().interrupt(); }
        };
        try {
            pool.execute(blocked);
            if (!firstStarted.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("first worker did not start"); }
            pool.execute(blocked);
            pool.execute(blocked);
            if (!bothStarted.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("second worker did not start"); }
            try {
                pool.execute(blocked);
                throw new IllegalStateException("expected rejection");
            } catch (final RejectedExecutionException expected) { System.out.println("rejected"); }
            release.countDown();
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) { throw new IllegalStateException("pool did not terminate"); }
            System.out.println("completed=" + pool.getCompletedTaskCount());
        } finally {
            release.countDown();
            pool.shutdownNow();
        }
    }
}
