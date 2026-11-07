import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;

/** Retains only the k largest values, including duplicates. @author Snehanjan Chatterjee */
public final class CoreTopK {
    /** Returns the k largest values in ascending order; zero requests an empty result. */
    public static int[] largest(final int[] values, final int k) {
        Objects.requireNonNull(values, "values");
        if (k < 0 || k > values.length) { throw new IllegalArgumentException("invalid k"); }
        final int[] result = new int[k];
        if (k == 0) { return result; }
        final PriorityQueue<Integer> retained = new PriorityQueue<>();
        for (final int value : values) {
            if (retained.size() < k) { retained.offer(value); }
            else if (value > retained.peek()) { retained.poll(); retained.offer(value); }
        }
        for (int index = 0; index < k; index++) { result[index] = retained.remove(); }
        return result;
    }
    /** Demonstrates candidate retention and final ordered extraction. */
    public static void main(final String[] args) {
        System.out.println(Arrays.toString(largest(new int[] {4, 1, 7, 5, 2, 6}, 3)));
    }
}
