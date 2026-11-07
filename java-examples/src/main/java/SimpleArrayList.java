import java.util.Arrays;

/** A bounded teaching dynamic array with null support and reference clearing. @author Snehanjan Chatterjee */
public final class SimpleArrayList<T> {
    private static final int MAX_CAPACITY = 1 << 20;
    private Object[] elements = new Object[2];
    private int size;

    /** Returns the logical element count. */
    public int size() { return size; }

    /** Reads one logical element; all typed writes preserve the cast invariant. */
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        checkElement(index);
        return (T) elements[index];
    }

    /** Replaces one element and returns its old value. */
    public T set(final int index, final T value) {
        final T previous = get(index);
        elements[index] = value;
        return previous;
    }

    /** Appends one value, growing only when necessary. */
    public void add(final T value) { add(size, value); }

    /** Inserts at any logical position, including the end. */
    public void add(final int index, final T value) {
        if (index < 0 || index > size) { throw new IndexOutOfBoundsException("index=" + index); }
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        size++;
    }

    /** Removes one element, shifts the suffix and clears the retained reference. */
    public T remove(final int index) {
        final T previous = get(index);
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return previous;
    }

    /** Releases references while retaining capacity for reuse. */
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    /** Rejects invalid element indexes before reading or mutating. */
    private void checkElement(final int index) {
        if (index < 0 || index >= size) { throw new IndexOutOfBoundsException("index=" + index); }
    }

    /** Doubles storage subject to the exercise's explicit capacity limit. */
    private void ensureCapacity() {
        if (size < elements.length) { return; }
        if (elements.length == MAX_CAPACITY) { throw new IllegalStateException("exercise capacity reached"); }
        elements = Arrays.copyOf(elements, Math.min(MAX_CAPACITY, elements.length * 2));
    }

    /** Demonstrates growth, insertion, removal and null values. */
    public static void main(final String[] args) {
        final SimpleArrayList<Integer> list = new SimpleArrayList<>();
        list.add(10); list.add(30); list.add(1, 20);
        System.out.println(list.remove(1));
        System.out.println(list.get(1));
        System.out.println(list.size());
        list.add(null);
        System.out.println(list.get(2));
    }
}
