import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/** Coordinate compound state changes and make cancellation cooperative. @author Snehanjan Chatterjee */
public final class LatchCoordination {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(2);
        final CountDownLatch done = new CountDownLatch(2);
        final AtomicInteger count = new AtomicInteger();
        try {
            for (int i = 0; i < 2; i++) { pool.submit(() -> { try { count.incrementAndGet(); } finally { done.countDown(); } }); }
            done.await();
            System.out.println(count.get());
        } finally { pool.shutdownNow(); }
    }
}
