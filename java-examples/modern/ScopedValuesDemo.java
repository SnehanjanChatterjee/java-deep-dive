/** Binds immutable context to a bounded dynamic scope. @author Snehanjan Chatterjee */
public final class ScopedValuesDemo {
    private static final ScopedValue<String> REQUEST = ScopedValue.newInstance();
    /** Shows that the binding exists only during the scoped action. */
    public static void main(final String[] args) {
        System.out.println(REQUEST.isBound());
        ScopedValue.where(REQUEST, "request-42").run(() -> System.out.println(REQUEST.get()));
        System.out.println(REQUEST.isBound());
    }
}
