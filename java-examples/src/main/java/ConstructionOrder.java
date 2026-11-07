/** Exposes dynamic dispatch before subclass initialization completes. @author Snehanjan Chatterjee */
public final class ConstructionOrder {
    private static class Base {
        /** Calls an overridable method to demonstrate a construction hazard. */
        Base() { report(); }
        /** Defines behavior the subtype overrides. */
        void report() { System.out.println("base"); }
    }
    private static final class Child extends Base {
        private final int value = initialValue();
        /** Computes a nonconstant initial value when the field initializer runs. */
        private static int initialValue() { return 7; }
        /** Runs after the superclass call and the child's field initializer. */
        Child() { System.out.println(value); }
        /** Reads the child's field before its initializer has necessarily run. */
        @Override void report() { System.out.println(value); }
    }
    /** Constructs one child to make the default and initialized values visible. */
    public static void main(final String[] args) { new Child(); }
}
