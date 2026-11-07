import java.util.Arrays;
import java.util.Objects;

/** Implements ordinary array traversal before array type-system puzzles. @author Snehanjan Chatterjee */
public final class ArrayBasics {
    /** Sums the components, detecting an int overflow rather than wrapping. */
    public static int sum(final int[] values) {
        Objects.requireNonNull(values, "values");
        int total = 0;
        for (int index = 0; index < values.length; index++) {
            total = Math.addExact(total, values[index]);
        }
        return total;
    }

    /** Returns the first matching index, or -1 when no component matches. */
    public static int firstIndexOf(final int[] values, final int target) {
        Objects.requireNonNull(values, "values");
        for (int index = 0; index < values.length; index++) {
            if (values[index] == target) {
                return index;
            }
        }
        return -1;
    }

    /** Counts every matching component, including repeated matches. */
    public static int count(final int[] values, final int target) {
        Objects.requireNonNull(values, "values");
        int matches = 0;
        for (final int value : values) {
            if (value == target) {
                matches++;
            }
        }
        return matches;
    }

    /** Shows indexing, default initialization, traversal and an independent copy. */
    public static void main(final String[] args) {
        final int[] scores = {4, 7, 4};
        final int[] zeros = new int[2];
        System.out.println(scores[0]);
        System.out.println(zeros[1]);
        System.out.println(sum(scores));
        System.out.println(firstIndexOf(scores, 4));
        final int[] copy = Arrays.copyOf(scores, scores.length);
        copy[0] = 9;
        System.out.println(scores[0]);
    }
}
