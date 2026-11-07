/** Models alternatives with records, a sealed family and record patterns. @author Snehanjan Chatterjee */
public final class ModernTypesDemo {
    sealed interface Result permits Success, Failure { }
    record Success(int value) implements Result {
        Success { if (value < 0) { throw new IllegalArgumentException("nonnegative value required"); } }
    }
    record Failure(String message) implements Result { }
    /** Exhaustively describes the permitted result alternatives. */
    static String describe(final Result result) {
        return switch (result) {
            case Success(final int value) -> "value=" + value;
            case Failure(final String message) -> "error=" + message;
        };
    }
    /** Demonstrates both alternatives and constructor validation. */
    public static void main(final String[] args) {
        System.out.println(describe(new Success(42)));
        System.out.println(describe(new Failure("missing")));
        try { new Success(-1); } catch (final IllegalArgumentException expected) { System.out.println("rejected"); }
    }
}
