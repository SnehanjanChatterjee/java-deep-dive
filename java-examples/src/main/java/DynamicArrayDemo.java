import java.util.Arrays;

/** Connect indexed access, shifting and amortized growth to executable code. @author Snehanjan Chatterjee */
public final class DynamicArrayDemo {
    static final class IntList {
        private int[] data = new int[2]; private int size;
        /** Appends and grows geometrically when full. */
        void add(final int value) { if (size == data.length) { data = Arrays.copyOf(data, data.length * 2); } data[size++] = value; }
        /** Returns a value at a valid logical index. */
        int get(final int index) { check(index); return data[index]; }
        /** Removes one value and preserves the sequence. */
        int remove(final int index) { check(index); final int old = data[index]; System.arraycopy(data, index + 1, data, index, size - index - 1); size--; return old; }
        /** Reports logical element count. */
        int size() { return size; }
        /** Rejects indices outside the logical sequence. */
        private void check(final int index) { if (index < 0 || index >= size) { throw new IndexOutOfBoundsException("index=" + index); } }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final IntList values = new IntList();
        values.add(10); values.add(20); values.add(30);
        System.out.println(values.remove(1));
        System.out.println(values.get(1));
        System.out.println(values.size());
    }
}
