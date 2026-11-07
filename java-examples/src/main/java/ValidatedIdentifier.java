
/** Recognize trust boundaries and preserve object invariants when data enters a system. @author Snehanjan Chatterjee */
public final class ValidatedIdentifier {
    /** Enforces the identifier's domain invariant. */
    static int requirePositive(final int value) { if (value <= 0) { throw new IllegalArgumentException("positive identifier required"); } return value; }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        System.out.println(requirePositive(42));
        try { requirePositive(0); } catch (final IllegalArgumentException expected) { System.out.println("rejected"); }
    }
}
