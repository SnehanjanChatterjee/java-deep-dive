package study.math;

/** A small pure operation used to learn project layout and assertions. @author Snehanjan Chatterjee */
public final class Calculator {
    /** Prevents construction of this stateless utility. */
    private Calculator() { }

    /** Adds two ints using ordinary Java wrapping arithmetic. */
    public static int add(final int left, final int right) { return left + right; }

    /** Runs the same operation without a test runner. */
    public static void main(final String[] args) { System.out.println(add(2, 3)); }
}
