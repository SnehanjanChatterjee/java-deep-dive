import java.util.Objects;

/** A growing circular deque using explicit size, without built-in deque storage. @author Snehanjan Chatterjee */
public final class RingDeque<T> {
    private static final int MAX_CAPACITY = 1 << 20;
    private Object[] values = new Object[2];
    private int head;
    private int size;

    /** Reports logical elements. */
    public int size() { return size; }

    /** Inserts a non-null value at the first end. */
    public void addFirst(final T value) {
        Objects.requireNonNull(value, "value");
        growIfFull();
        head = (head - 1 + values.length) % values.length;
        values[head] = value;
        size++;
    }

    /** Inserts a non-null value at the last end. */
    public void addLast(final T value) {
        Objects.requireNonNull(value, "value");
        growIfFull();
        values[(head + size) % values.length] = value;
        size++;
    }

    /** Removes the first value or returns null when empty. */
    @SuppressWarnings("unchecked")
    public T pollFirst() {
        if (size == 0) { return null; }
        final T value = (T) values[head];
        values[head] = null;
        head = (head + 1) % values.length;
        size--;
        return value;
    }

    /** Removes the last value or returns null when empty. */
    @SuppressWarnings("unchecked")
    public T pollLast() {
        if (size == 0) { return null; }
        final int index = (head + size - 1) % values.length;
        final T value = (T) values[index];
        values[index] = null;
        size--;
        return value;
    }

    /** Copies logical order into doubled storage, keeping all ring transitions valid. */
    private void growIfFull() {
        if (size < values.length) { return; }
        if (size == MAX_CAPACITY) { throw new IllegalStateException("exercise capacity reached"); }
        final Object[] expanded = new Object[values.length * 2];
        for (int index = 0; index < size; index++) { expanded[index] = values[(head + index) % values.length]; }
        values = expanded;
        head = 0;
    }

    /** Demonstrates wraparound, growth and both endpoint removals. */
    public static void main(final String[] args) {
        final RingDeque<String> deque = new RingDeque<>();
        deque.addLast("A"); deque.addLast("B");
        System.out.println(deque.pollFirst());
        deque.addLast("C"); deque.addFirst("start");
        System.out.println(deque.pollLast());
        System.out.println(deque.pollFirst());
        System.out.println(deque.pollFirst());
        System.out.println(deque.pollFirst());
    }
}
