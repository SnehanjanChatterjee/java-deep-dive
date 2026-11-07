import java.util.Arrays;
import java.util.Objects;

/** Implements first-position search under an explicit sorted-array precondition. @author Snehanjan Chatterjee */
public final class ArraySearch {
    /** Returns the first position at least target in a nondecreasing array. */
    public static int lowerBound(final int[] sorted, final int target) {
        Objects.requireNonNull(sorted, "sorted");
        int low = 0;
        int high = sorted.length;
        while (low < high) {
            final int middle = low + (high - low) / 2;
            if (sorted[middle] < target) {
                low = middle + 1;
            } else {
                high = middle;
            }
        }
        return low;
    }
    /** Contrasts a first equal position with an encoded unsuccessful search. */
    public static void main(final String[] args) {
        final int[] sorted = {2, 4, 4, 7};
        System.out.println(lowerBound(sorted, 4));
        final int result = Arrays.binarySearch(sorted, 5);
        System.out.println(result);
        System.out.println(-result - 1);
    }
}
