/** Compares optimization constraints without claiming a timing result. @author Snehanjan Chatterjee */
public final class CoreHotLoop {
    private static final class Pair {
        private final int left;
        private final int right;
        /** Initializes a small immutable value. */
        private Pair(final int left, final int right) { this.left = left; this.right = right; }
    }
    private static volatile Pair published;
    /** Creates a locally used value whose allocation may be eliminated. */
    private static int localScore(final int seed) {
        final Pair pair = new Pair(seed, seed + 1);
        return pair.left * 31 + pair.right;
    }
    /** Publishes the value, making this a different observable operation. */
    private static int publishedScore(final int seed) {
        final Pair pair = new Pair(seed, seed + 1);
        published = pair;
        return pair.left * 31 + pair.right;
    }
    /** Repeats calls to support compilation inspection, never reporting elapsed-time claims. */
    public static void main(final String[] args) {
        long local = 0;
        long escaped = 0;
        for (int iteration = 0; iteration < 200_000; iteration++) { local += localScore(iteration & 1023); }
        for (int iteration = 0; iteration < 200_000; iteration++) { escaped += publishedScore(iteration & 1023); }
        System.out.println(local == escaped);
        System.out.println(published != null);
    }
}
