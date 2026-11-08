/** Observes short-circuit evaluation and left-associative text concatenation. */
public final class ExpressionOrderLesson {
    /** Uses statements in main without additional helper methods. */
    public static void main(final String[] args) {
        int checks = 0;
        final boolean skipped = false && ++checks > 0;
        System.out.println(skipped);
        System.out.println(checks);
        final boolean evaluated = false & ++checks > 0;
        System.out.println(evaluated);
        System.out.println(checks);
        System.out.println("sum=" + 1 + 2);
        System.out.println("sum=" + (1 + 2));
    }
}
