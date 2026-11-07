/** Binds immutable context to a bounded dynamic scope. @author Snehanjan Chatterjee */
public final class ScopedValuesDemo {
    private static final ScopedValue<String> REQUEST = ScopedValue.newInstance();
    /** Reads the request from an indirectly invoked formatter. */
    private static String format() { return REQUEST.get(); }
    /** Calls a formatter without an explicit context-forwarding parameter. */
    private static void report() { System.out.println(format()); }
    /** Shows that the binding exists only during the scoped action. */
    public static void main(final String[] args) {
        System.out.println(REQUEST.isBound());
        ScopedValue.where(REQUEST, "request-42").run(ScopedValuesDemo::report);
        System.out.println(REQUEST.isBound());
    }
}
