import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Creates inspectable contention and a bounded retaining path. @author Snehanjan Chatterjee */
public final class DiagnosticLab {
    private static final Object MONITOR = new Object();
    private static final List<byte[]> RETAINED = new ArrayList<>();

    /** Holds a monitor while awaiting an explicit release signal. */
    private static void hold(final CountDownLatch owned, final CountDownLatch release) {
        synchronized (MONITOR) {
            owned.countDown();
            try { release.await(); }
            catch (final InterruptedException interrupted) { Thread.currentThread().interrupt(); }
        }
    }

    /** Acquires the same monitor after the owner releases it. */
    private static void contend() {
        synchronized (MONITOR) { System.out.println("waiter acquired monitor"); }
    }

    /** Waits for interactive investigation, then releases workers and retains a final recording window. */
    public static void main(final String[] args) throws InterruptedException, IOException {
        for (int i = 0; i < 16; i++) { RETAINED.add(new byte[64 * 1024]); }
        final CountDownLatch owned = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        final Thread owner = new Thread(() -> hold(owned, release), "study-owner");
        final Thread waiter = new Thread(DiagnosticLab::contend, "study-waiter");
        owner.start();
        try {
            if (!owned.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("owner did not acquire monitor"); }
            waiter.start();
            System.out.println("PID=" + ProcessHandle.current().pid());
            System.out.println("Inspect the process; press Enter to release the monitor.");
            System.in.read();
        } finally {
            release.countDown();
            owner.join();
            waiter.join();
        }
        System.out.println("Locks released. Capture the final JFR recording; press Enter to finish.");
        System.in.read();
        RETAINED.clear();
    }
}
