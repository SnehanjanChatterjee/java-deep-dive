import java.util.function.Supplier;

/** Separates creation of a bound reference from later callback execution. @author Snehanjan Chatterjee */
public final class CallbackTiming {
    private static final class Scope {
        private final String name = "enclosing";
        /** Uses the enclosing receiver as lambda this. */
        Supplier<String> lambda() { return () -> this.name; }
        /** Supplies a different receiver and field for an anonymous implementation. */
        Supplier<String> anonymous() {
            return new Supplier<String>() {
                private final String name = "anonymous";
                @Override public String get() { return this.name; }
            };
        }
    }
    private static class Greeter {
        /** Supplies a superclass body for a bound super reference. */
        String greet() { return "base"; }
    }
    private static final class Child extends Greeter {
        /** Overrides ordinary virtual dispatch. */
        @Override String greet() { return "child"; }
        /** Binds the superclass implementation instead of recursively calling the override. */
        Supplier<String> baseGreeting() { return super::greet; }
    }
    /** Shows null-receiver timing and the implementation selected by super. */
    public static void main(final String[] args) {
        final String missing = null;
        try {
            final Supplier<String> bound = missing::trim;
            throw new IllegalStateException("bound creation unexpectedly succeeded: " + bound);
        } catch (final NullPointerException expected) { System.out.println("bound reference: creation failed"); }
        final Supplier<String> deferred = () -> missing.trim();
        System.out.println("lambda created");
        try { deferred.get(); throw new IllegalStateException("expected invocation failure"); }
        catch (final NullPointerException expected) { System.out.println("lambda: invocation failed"); }
        final Child child = new Child();
        final Greeter parentView = child;
        final Supplier<String> virtual = parentView::greet;
        System.out.println(virtual.get() + "/" + child.baseGreeting().get());
        final Scope scope = new Scope();
        System.out.println("this=" + scope.lambda().get() + "/" + scope.anonymous().get());
    }
}
