import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/** A one-way shared synchronizer used to study AQS state and waiting. @author Snehanjan Chatterjee */
public final class OneShotSignal {
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;
        /** Allows every shared acquirer once the signal is open. */
        @Override protected int tryAcquireShared(final int ignored) { return getState() == 1 ? 1 : -1; }
        /** Publishes the sole closed-to-open transition; repeated opens change nothing. */
        @Override protected boolean tryReleaseShared(final int ignored) { return compareAndSetState(0, 1); }
    }
    private final Sync sync = new Sync();

    /** Waits interruptibly until the one-way signal is open. */
    public void await() throws InterruptedException { sync.acquireSharedInterruptibly(1); }

    /** Waits for at most the supplied nonnegative duration, returning false on timeout. */
    public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
        Objects.requireNonNull(unit, "unit");
        if (timeout < 0) { throw new IllegalArgumentException("negative timeout"); }
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /** Opens the signal once and releases eligible current/future shared acquirers. */
    public boolean open() { return sync.releaseShared(1); }

    /** Demonstrates publication through release/acquire and idempotent opening. */
    public static void main(final String[] args) throws InterruptedException {
        final OneShotSignal signal = new OneShotSignal();
        final int[] payload = new int[1];
        final Thread writer = new Thread(() -> { payload[0] = 42; signal.open(); });
        writer.start();
        try {
            if (!signal.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("writer did not open signal"); }
            System.out.println(payload[0]);
            System.out.println(signal.open());
        } finally {
            writer.interrupt();
            writer.join();
        }
    }
}
