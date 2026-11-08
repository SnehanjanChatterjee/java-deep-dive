import java.util.concurrent.locks.ReentrantLock;

/** Traces matched nested acquisitions by one owning thread. @author Snehanjan Chatterjee */
public final class ReentrantLockDepthLesson {
    /** Returns hold counts at outer, inner, released-inner and released-outer boundaries. */
    public static int[] depths() {
        final ReentrantLock lock = new ReentrantLock();
        final int[] counts = new int[4];
        lock.lock();
        try {
            counts[0] = lock.getHoldCount();
            lock.lock();
            try {
                counts[1] = lock.getHoldCount();
            } finally {
                lock.unlock();
            }
            counts[2] = lock.getHoldCount();
        } finally {
            lock.unlock();
        }
        counts[3] = lock.getHoldCount();
        return counts;
    }

    /** A contender is excluded until the final matching unlock, not merely the first. */
    public static void main(final String[] args) {
        for (final int depth : depths()) {
            System.out.println(depth);
        }
    }
}
