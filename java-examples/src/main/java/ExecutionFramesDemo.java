/** A small method whose bytecode exposes locals and operand-stack operations. @author Snehanjan Chatterjee */
public final class ExecutionFramesDemo {
    /** Returns twice the int argument, with ordinary Java int overflow semantics. */
    public static int twice(final int value) {
        return value * 2;
    }

    /** Subtracts the right argument from the left with ordinary int semantics. */
    public static int difference(final int left, final int right) {
        return left - right;
    }

    /** Supplies the value traced in the bytecode lesson. */
    public static void main(final String[] args) {
        System.out.println(twice(3));
    }
}
