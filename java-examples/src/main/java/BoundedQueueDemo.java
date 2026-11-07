import java.util.Objects;

/** Choose coordination around the complete invariant, including backpressure. @author Snehanjan Chatterjee */
public final class BoundedQueueDemo {
    static final class Queue<T> {
        private final Object[] slots; private int head; private int tail; private int size;
        /** Creates a positive-capacity circular queue. */
        Queue(final int capacity) { if (capacity < 1) { throw new IllegalArgumentException("positive capacity required"); } slots = new Object[capacity]; }
        /** Waits for capacity before inserting a non-null item. */
        synchronized void put(final T value) throws InterruptedException { Objects.requireNonNull(value); while (size == slots.length) { wait(); } slots[tail] = value; tail = (tail + 1) % slots.length; size++; notifyAll(); }
        /** Waits for an item, removes its retained reference, and signals producers. */
        @SuppressWarnings("unchecked")
        synchronized T take() throws InterruptedException { while (size == 0) { wait(); } final T value = (T) slots[head]; slots[head] = null; head = (head + 1) % slots.length; size--; notifyAll(); return value; }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Queue<String> queue = new Queue<>(2);
        queue.put("first"); queue.put("second");
        System.out.println(queue.take());
        queue.put("third");
        System.out.println(queue.take());
        System.out.println(queue.take());
    }
}
