import java.util.concurrent.locks.ReentrantReadWriteLock;

/** Retains read ownership across a write-to-read transition. @author Snehanjan Chatterjee */
public final class ReadWriteDowngradeLesson {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int value;

    /** Installs a value, downgrades without an unlocked gap, then returns its protected observation. */
    public int updateAndRead(final int replacement) {
        lock.writeLock().lock();
        try {
            value = replacement;
            lock.readLock().lock();
        } finally {
            lock.writeLock().unlock();
        }
        try {
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    /** Reports only this caller's remaining holds for the cleanup demonstration. */
    public boolean callerHasNoHolds() {
        return !lock.isWriteLockedByCurrentThread() && lock.getReadHoldCount() == 0;
    }

    /** Demonstrates a protected observation and complete release afterward. */
    public static void main(final String[] args) {
        final ReadWriteDowngradeLesson state = new ReadWriteDowngradeLesson();
        System.out.println("value=" + state.updateAndRead(42));
        System.out.println("caller released=" + state.callerHasNoHolds());
    }
}
