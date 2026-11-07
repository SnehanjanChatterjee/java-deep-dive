/** Shows abrupt completion across ordinary method calls. @author Snehanjan Chatterjee */
public final class ExceptionFlow {
    /** Parses an integer or propagates NumberFormatException. */
    private static int parse(final String text) {
        System.out.println("parse");
        return Integer.parseInt(text);
    }
    /** Returns the parsed value doubled when parsing succeeds. */
    private static int doubleInput(final String text) {
        System.out.println("doubleInput");
        final int value = parse(text);
        System.out.println("parsed");
        return Math.multiplyExact(value, 2);
    }
    /** Catches the failure after the two inner invocations complete abruptly. */
    public static void main(final String[] args) {
        try {
            System.out.println(doubleInput("bad"));
            System.out.println("success");
        } catch (final NumberFormatException failure) {
            System.out.println("invalid integer");
        }
        System.out.println("after handler");
    }
}
