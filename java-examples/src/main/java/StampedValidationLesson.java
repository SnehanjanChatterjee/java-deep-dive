import java.util.concurrent.locks.StampedLock;

/** Isolates optimistic validation failure by deliberately inserting a write. @author Snehanjan Chatterjee */
public final class StampedValidationLesson {
    /** Returns the rejected tentative value and the value reread under genuine read ownership. */
    public static String[] observations() {
        final StampedLock lock = new StampedLock();
        final int[] state = {0};
        final long optimistic = lock.tryOptimisticRead();
        final int tentative = state[0];
        final long write = lock.writeLock();
        try {
            state[0] = 42;
        } finally {
            lock.unlockWrite(write);
        }
        final boolean valid = lock.validate(optimistic);
        int selected = tentative;
        if (!valid) {
            final long read = lock.readLock();
            try {
                selected = state[0];
            } finally {
                lock.unlockRead(read);
            }
        }
        return new String[] {"tentative=" + tentative, "valid=" + valid, "selected=" + selected};
    }

    /** This is a deterministic protocol experiment, not a claim of concurrent execution. */
    public static void main(final String[] args) {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
