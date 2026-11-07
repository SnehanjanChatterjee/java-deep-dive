import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

/** Separates safe publication from a deliberately forced lost update. @author Snehanjan Chatterjee */
public final class SafePublicationLab {
    private static final class Snapshot {
        private final int value;
        /** Establishes immutable state before publication. */
        private Snapshot(final int value) { this.value = value; }
    }
    private volatile Snapshot published;
    private volatile int count;

    /** Forces both workers to read before either writes the incremented value. */
    private void splitIncrement(final CyclicBarrier readsComplete, final AtomicReference<Throwable> failure) {
        final int previous = count;
        try {
            readsComplete.await();
            count = previous + 1;
        } catch (final InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            failure.compareAndSet(null, interrupted);
        } catch (final BrokenBarrierException broken) {
            failure.compareAndSet(null, broken);
        }
    }

    /** Runs the publication and coordinated lost-update demonstrations. */
    public static void main(final String[] args) throws InterruptedException {
        final SafePublicationLab lab = new SafePublicationLab();
        final Thread writer = new Thread(() -> lab.published = new Snapshot(42));
        writer.start();
        Snapshot observed;
        while ((observed = lab.published) == null) { Thread.yield(); }
        System.out.println(observed.value);
        writer.join();
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        final Thread first = new Thread(() -> lab.splitIncrement(barrier, failure));
        final Thread second = new Thread(() -> lab.splitIncrement(barrier, failure));
        first.start(); second.start(); first.join(); second.join();
        if (failure.get() != null) { throw new IllegalStateException("demonstration failed", failure.get()); }
        System.out.println("lost update=" + lab.count);
    }
}
