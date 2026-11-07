/** Demonstrates restoration on nested normal and exceptional scope exits. @author Snehanjan Chatterjee */
public final class ScopedNestingLesson {
    private static final ScopedValue<String> CONTEXT = ScopedValue.newInstance();
    /** Reads nested bindings and confirms restoration after failure. */
    public static void main(final String[] args) {
        ScopedValue.where(CONTEXT, "outer").run(() -> {
            System.out.println(CONTEXT.get());
            try {
                ScopedValue.where(CONTEXT, "inner").run(() -> {
                    System.out.println(CONTEXT.get());
                    throw new IllegalStateException("stop inner");
                });
            } catch (final IllegalStateException expected) { System.out.println(CONTEXT.get()); }
        });
        System.out.println("bound=" + CONTEXT.isBound());
    }
}
