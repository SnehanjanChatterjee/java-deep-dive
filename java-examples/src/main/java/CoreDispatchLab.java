/** Separates compile-time overload selection from receiver dispatch. @author Snehanjan Chatterjee */
public final class CoreDispatchLab {
    private static class Parent {
        /** Defines the signature visible through a Parent reference. */
        String describe(final Object value) { return "parent-object"; }
    }
    private static final class Child extends Parent {
        /** Overrides the already selected Object signature. */
        @Override String describe(final Object value) { return "child-object"; }
        /** Adds an overload unavailable through the Parent declaration. */
        String describe(final String value) { return "child-string"; }
    }
    /** Demonstrates fixed-arity primitive widening. */
    private static String choose(final long value) { return "long"; }
    /** Demonstrates reference identity conversion of an existing wrapper. */
    private static String choose(final Integer value) { return "Integer"; }
    /** Demonstrates the later variable-arity phase. */
    private static String choose(final int... values) { return "varargs"; }
    /** Prints the signatures and implementations selected for five calls. */
    public static void main(final String[] args) {
        final Parent receiver = new Child();
        final Object argument = "text";
        System.out.println(receiver.describe(argument));
        System.out.println(((Child) receiver).describe("text"));
        System.out.println(choose(1));
        System.out.println(choose(Integer.valueOf(1)));
        System.out.println(choose());
    }
}
