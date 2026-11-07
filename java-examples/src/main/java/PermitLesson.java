import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/** Separates resource permits from the number of running threads. @author Snehanjan Chatterjee */
public final class PermitLesson {
    private static final class Occupancy {
        private int active;
        private int maximum;
        /** Counts one acquired resource user under a monitor. */
        private synchronized void enter() { active++; maximum = Math.max(maximum, active); }
        /** Removes one finished resource user. */
        private synchronized void leave() { active--; }
    }
    private static final class Worker implements Runnable {
        private final Semaphore permits;
        private final CountDownLatch entered;
        private final CountDownLatch release;
        private final Occupancy occupancy;
        private InterruptedException failure;
        /** Retains the shared admission and demonstration signals. */
        private Worker(final Semaphore permits, final CountDownLatch entered,
                final CountDownLatch release, final Occupancy occupancy) {
            this.permits = permits; this.entered = entered; this.release = release; this.occupancy = occupancy;
        }
        /** Releases a permit only inside the region after successful acquisition. */
        @Override public void run() {
            try {
                permits.acquire();
                try {
                    occupancy.enter();
                    entered.countDown();
                    try { release.await(); }
                    finally { occupancy.leave(); }
                } finally { permits.release(); }
            } catch (final InterruptedException cancelled) {
                failure = cancelled; Thread.currentThread().interrupt();
            }
        }
    }
    /** Establishes occupancy two, then lets all three workers complete. */
    public static void main(final String[] args) throws InterruptedException {
        final Semaphore permits = new Semaphore(2);
        final CountDownLatch entered = new CountDownLatch(2);
        final CountDownLatch release = new CountDownLatch(1);
        final Occupancy occupancy = new Occupancy();
        final Worker[] tasks = new Worker[3];
        final Thread[] threads = new Thread[3];
        for (int index = 0; index < threads.length; index++) {
            tasks[index] = new Worker(permits, entered, release, occupancy);
            threads[index] = new Thread(tasks[index]); threads[index].start();
        }
        try {
            if (!entered.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("two users did not enter"); }
            release.countDown();
            for (final Thread thread : threads) { thread.join(); }
            for (final Worker task : tasks) {
                if (task.failure != null) { throw new IllegalStateException("resource use interrupted", task.failure); }
            }
            System.out.println("maximum=" + occupancy.maximum);
            System.out.println("permits=" + permits.availablePermits());
        } finally {
            release.countDown();
            for (final Thread thread : threads) { thread.interrupt(); }
            for (final Thread thread : threads) { thread.join(); }
        }
    }
}
