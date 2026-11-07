import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/** Computes disjoint range sums with an explicit fork/join pool. @author Snehanjan Chatterjee */
public final class ForkJoinRangeSum {
    private static final class Sum extends RecursiveTask<Long> {
        private static final long serialVersionUID = 1L;
        private final int[] values;
        private final int from;
        private final int to;
        private final int threshold;
        /** Defines an immutable half-open work range. */
        private Sum(final int[] values, final int from, final int to, final int threshold) {
            this.values = values; this.from = from; this.to = to; this.threshold = threshold;
        }
        /** Splits only above the threshold, then combines disjoint partial results. */
        @Override protected Long compute() {
            if (to - from <= threshold) {
                long total = 0;
                for (int index = from; index < to; index++) { total += values[index]; }
                return total;
            }
            final int middle = from + (to - from) / 2;
            final Sum left = new Sum(values, from, middle, threshold);
            final Sum right = new Sum(values, middle, to, threshold);
            left.fork();
            final long rightTotal = right.compute();
            return left.join() + rightTotal;
        }
    }
    /** Sums a caller-stable input with a positive sequential threshold. */
    public static long sum(final int[] input, final int threshold) {
        Objects.requireNonNull(input, "input");
        if (threshold < 1) { throw new IllegalArgumentException("positive threshold required"); }
        final ForkJoinPool pool = new ForkJoinPool(2);
        try { return pool.invoke(new Sum(input, 0, input.length, threshold)); }
        finally { pool.shutdown(); }
    }
    /** Demonstrates positive/negative inputs and the empty identity. */
    public static void main(final String[] args) {
        System.out.println(sum(new int[] {1, 2, 3, 4, -5}, 2));
        System.out.println(sum(new int[0], 2));
    }
}
