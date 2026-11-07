import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/** A timed single-slot mailbox with separate producer and consumer condition queues. @author Snehanjan Chatterjee */
public final class ConditionMailbox<T> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private T value;
    private boolean closed;
    /** Waits interruptibly for a free slot and publishes a non-null item. */
    public void put(final T item) throws InterruptedException {
        Objects.requireNonNull(item, "item");
        lock.lockInterruptibly();
        try {
            while (value != null && !closed) { notFull.await(); }
            if (closed) { throw new IllegalStateException("mailbox closed"); }
            value = item; notEmpty.signal();
        } finally { lock.unlock(); }
    }
    /** Uses one total budget for lock acquisition and condition waiting; null means no delivered item. */
    public T poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        Objects.requireNonNull(unit, "unit");
        if (timeout < 0) { throw new IllegalArgumentException("negative timeout"); }
        final long budget = unit.toNanos(timeout);
        final long started = System.nanoTime();
        if (!lock.tryLock(budget, TimeUnit.NANOSECONDS)) { return null; }
        try {
            long remaining = budget - (System.nanoTime() - started);
            while (value == null && !closed) {
                if (remaining <= 0) { return null; }
                remaining = notEmpty.awaitNanos(remaining);
            }
            final T result = value; value = null; notFull.signal(); return result;
        } finally { lock.unlock(); }
    }
    /** Wakes every kind of waiter because closure changes both guarded predicates. */
    public void close() {
        lock.lock();
        try { closed = true; notEmpty.signalAll(); notFull.signalAll(); }
        finally { lock.unlock(); }
    }
    /** Demonstrates timeout, successful retrieval and terminal closure. */
    public static void main(final String[] args) throws InterruptedException {
        final ConditionMailbox<String> mailbox = new ConditionMailbox<>();
        System.out.println(mailbox.poll(0, TimeUnit.NANOSECONDS));
        mailbox.put("message"); System.out.println(mailbox.poll(1, TimeUnit.SECONDS));
        mailbox.close(); System.out.println(mailbox.poll(1, TimeUnit.SECONDS));
    }
}
