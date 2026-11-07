import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

/** Computes fixed-window maxima using candidate indexes in a monotonic deque. @author Snehanjan Chatterjee */
public final class CoreSlidingWindow {
    /** Returns one maximum per full window; requires 1 <= window <= values.length. */
    public static int[] maxima(final int[] values, final int window) {
        Objects.requireNonNull(values, "values");
        if (window < 1 || window > values.length) { throw new IllegalArgumentException("invalid window"); }
        final int[] result = new int[values.length - window + 1];
        final Deque<Integer> candidates = new ArrayDeque<>();
        for (int index = 0; index < values.length; index++) {
            final int firstAllowed = index - window + 1;
            while (!candidates.isEmpty() && candidates.peekFirst() < firstAllowed) { candidates.removeFirst(); }
            while (!candidates.isEmpty() && values[candidates.peekLast()] <= values[index]) { candidates.removeLast(); }
            candidates.addLast(index);
            if (firstAllowed >= 0) { result[firstAllowed] = values[candidates.peekFirst()]; }
        }
        return result;
    }
    /** Demonstrates overlapping windows without rescanning each window. */
    public static void main(final String[] args) {
        System.out.println(Arrays.toString(maxima(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }
}
