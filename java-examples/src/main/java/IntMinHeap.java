import java.util.Arrays;
import java.util.NoSuchElementException;

/** A single-file binary min-heap without a built-in queue. @author Snehanjan Chatterjee */
public final class IntMinHeap {
    private static final int MAX_CAPACITY = 1 << 20;
    private int[] values = new int[4];
    private int size;

    /** Reports the number of stored values. */
    public int size() { return size; }

    /** Returns the least value without changing the heap. */
    public int peek() {
        if (size == 0) { throw new NoSuchElementException("empty heap"); }
        return values[0];
    }

    /** Appends and sifts up to preserve parent <= child. */
    public void offer(final int value) {
        if (size == values.length) {
            if (size == MAX_CAPACITY) { throw new IllegalStateException("exercise capacity reached"); }
            values = Arrays.copyOf(values, Math.min(MAX_CAPACITY, values.length * 2));
        }
        int hole = size++;
        while (hole > 0) {
            final int parent = (hole - 1) / 2;
            if (values[parent] <= value) { break; }
            values[hole] = values[parent];
            hole = parent;
        }
        values[hole] = value;
    }

    /** Removes the root and sifts its replacement down through smaller children. */
    public int poll() {
        final int minimum = peek();
        final int replacement = values[--size];
        if (size == 0) { return minimum; }
        int hole = 0;
        while (hole < size / 2) {
            int child = hole * 2 + 1;
            if (child + 1 < size && values[child + 1] < values[child]) { child++; }
            if (replacement <= values[child]) { break; }
            values[hole] = values[child];
            hole = child;
        }
        values[hole] = replacement;
        return minimum;
    }

    /** Demonstrates sorted removal from a partially ordered heap. */
    public static void main(final String[] args) {
        final IntMinHeap heap = new IntMinHeap();
        for (final int value : new int[] {4, 1, 3, 2}) { heap.offer(value); }
        while (heap.size() > 0) { System.out.println(heap.poll()); }
    }
}
