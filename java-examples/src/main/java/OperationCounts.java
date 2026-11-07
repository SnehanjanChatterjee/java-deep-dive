/** Counts explicit loop-body work to distinguish growth from elapsed time. @author Snehanjan Chatterjee */
public final class OperationCounts {
    /** Counts one unit for each of n items; rejects a negative problem size. */
    public static long linear(final int n) {
        if (n < 0) { throw new IllegalArgumentException("negative size"); }
        long count = 0;
        for (int i = 0; i < n; i++) { count++; }
        return count;
    }
    /** Counts one unit for each ordered pair in an n by n traversal. */
    public static long quadratic(final int n) {
        if (n < 0) { throw new IllegalArgumentException("negative size"); }
        long count = 0;
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) { count++; }
        }
        return count;
    }
    /** Prints size, linear work and quadratic work for small inputs. */
    public static void main(final String[] args) {
        for (final int n : new int[] {1, 2, 4, 8}) { System.out.println(n + ":" + linear(n) + ":" + quadratic(n)); }
    }
}
