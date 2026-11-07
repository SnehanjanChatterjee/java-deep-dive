
/** Make contracts explicit and use tests to protect meaningful behavior. @author Snehanjan Chatterjee */
public final class ContractExample {
    /** Adds valid nonnegative values and detects overflow. */
    static int subtotal(final int left, final int right) { if (left < 0 || right < 0) { throw new IllegalArgumentException("nonnegative values required"); } return Math.addExact(left, right); }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        System.out.println(subtotal(3, 4));
        System.out.println(subtotal(0, 4));
        try { subtotal(-1, 4); } catch (final IllegalArgumentException expected) { System.out.println("rejected"); }
    }
}
