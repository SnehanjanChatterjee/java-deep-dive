import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/** A bounded FIFO with explicit close-and-drain semantics. @author Snehanjan Chatterjee */
public final class CloseableWorkQueue<T> {
    private final Object[] elements;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private int head;
    private int tail;
    private int size;
    private boolean closed;
    /** Creates positive-capacity, non-null-element storage. */
    public CloseableWorkQueue(final int capacity) {
        if (capacity < 1) { throw new IllegalArgumentException("positive capacity required"); }
        elements = new Object[capacity];
    }
    /** Waits for space, then inserts, unless closure wins before insertion. */
    public void put(final T value) throws InterruptedException {
        Objects.requireNonNull(value, "value");
        lock.lockInterruptibly();
        try {
            while (size == elements.length && !closed) { notFull.await(); }
            if (closed) { throw new IllegalStateException("queue closed"); }
            elements[tail] = value; tail = (tail + 1) % elements.length; size++;
            notEmpty.signal();
        } finally { lock.unlock(); }
    }
    /** Drains queued values after closure; null denotes closed and empty. */
    @SuppressWarnings("unchecked")
    public T take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (size == 0 && !closed) { notEmpty.await(); }
            if (size == 0) { return null; }
            final T value = (T) elements[head];
            elements[head] = null; head = (head + 1) % elements.length; size--;
            notFull.signal();
            return value;
        } finally { lock.unlock(); }
    }
    /** Rejects future puts and wakes every waiter to recheck closure. */
    public void close() {
        lock.lock();
        try { closed = true; notEmpty.signalAll(); notFull.signalAll(); }
        finally { lock.unlock(); }
    }
    /** Demonstrates FIFO draining and the terminal sentinel. */
    public static void main(final String[] args) throws InterruptedException {
        final CloseableWorkQueue<String> queue = new CloseableWorkQueue<>(2);
        queue.put("A"); queue.put("B"); queue.close();
        System.out.println(queue.take()); System.out.println(queue.take()); System.out.println(queue.take());
    }
}
