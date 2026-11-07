import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** Bounds actual resource occupancy while allowing virtual-thread-per-task execution. @author Snehanjan Chatterjee */
public final class VirtualResourceLimitLesson {
    /** Coordinates two admitted tasks before releasing six total tasks to completion. */
    public static void main(final String[] args) throws Exception {
        final Semaphore permits = new Semaphore(2);
        final AtomicInteger active = new AtomicInteger();
        final AtomicInteger maximum = new AtomicInteger();
        final CountDownLatch entered = new CountDownLatch(2);
        final CountDownLatch release = new CountDownLatch(1);
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            try {
                final List<Future<Integer>> results = new ArrayList<>();
                for (int index = 0; index < 6; index++) {
                    results.add(executor.submit(() -> {
                        permits.acquire();
                        try {
                            final int occupancy = active.incrementAndGet();
                            maximum.accumulateAndGet(occupancy, Math::max);
                            entered.countDown();
                            try { release.await(); return 7; }
                            finally { active.decrementAndGet(); }
                        } finally { permits.release(); }
                    }));
                }
                if (!entered.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("resource occupancy not established"); }
                release.countDown();
                int total = 0;
                for (final Future<Integer> result : results) { total += result.get(); }
                System.out.println("maximum=" + maximum.get());
                System.out.println("total=" + total);
                System.out.println("permits=" + permits.availablePermits());
            } finally { release.countDown(); }
        }
    }
}
