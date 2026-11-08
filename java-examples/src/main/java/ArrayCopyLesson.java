import java.util.Arrays;

/** Contrasts overlapping in-place array copies with allocating a larger array. */
public final class ArrayCopyLesson {
    /** Shows an overlap-safe shift and zero-filled expansion. */
    public static void main(final String[] args) {
        final int[] values = {1, 2, 3, 4};
        System.arraycopy(values, 0, values, 1, 3);
        System.out.println(Arrays.toString(values));
        final int[] expanded = Arrays.copyOf(values, 6);
        System.out.println(Arrays.toString(expanded));
        System.out.println(values == expanded);
    }
}
