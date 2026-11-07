import java.util.Objects;

/** A closeable single-slot mailbox using one intrinsic monitor and guarded predicates. @author Snehanjan Chatterjee */
public final class MonitorMailbox<T> {
    private T value;
    private boolean closed;
    /** Waits for capacity, then publishes a non-null value unless closed. */
    public synchronized void put(final T item) throws InterruptedException {
        Objects.requireNonNull(item, "item");
        while (value != null && !closed) { wait(); }
        if (closed) { throw new IllegalStateException("mailbox closed"); }
        value = item; notifyAll();
    }
    /** Drains a value or returns null only after closure and exhaustion. */
    public synchronized T take() throws InterruptedException {
        while (value == null && !closed) { wait(); }
        final T result = value;
        value = null;
        notifyAll();
        return result;
    }
    /** Rejects future writes while allowing an existing value to be drained. */
    public synchronized void close() { closed = true; notifyAll(); }
    /** Demonstrates the declared drain-on-close contract. */
    public static void main(final String[] args) throws InterruptedException {
        final MonitorMailbox<String> mailbox = new MonitorMailbox<>();
        mailbox.put("message"); mailbox.close();
        System.out.println(mailbox.take()); System.out.println(mailbox.take());
        try { mailbox.put("late"); }
        catch (final IllegalStateException expected) { System.out.println("closed"); }
    }
}
