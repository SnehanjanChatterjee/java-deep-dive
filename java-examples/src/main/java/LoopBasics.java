/** Traces decisions and repetition without a collection API. @author Snehanjan Chatterjee */
public final class LoopBasics {
    /** Sums the integers from one through the inclusive nonnegative limit. */
    public static int sumThrough(final int limit) {
        if (limit < 0 || limit > 65_535) {
            throw new IllegalArgumentException("limit must be between 0 and 65535");
        }
        int total = 0;
        for (int number = 1; number <= limit; number++) {
            total += number;
        }
        return total;
    }

    /** Shows a branch, an accumulator and a condition-controlled loop. */
    public static void main(final String[] args) {
        final int score = 76;
        if (score >= 80) {
            System.out.println("distinction");
        } else if (score >= 50) {
            System.out.println("pass");
        } else {
            System.out.println("retry");
        }
        System.out.println(sumThrough(4));
        int remaining = 3;
        while (remaining > 0) {
            System.out.println(remaining);
            remaining--;
        }
    }
}
