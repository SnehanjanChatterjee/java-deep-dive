import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Separates discovering a member, constructing a receiver and invoking behavior. @author Snehanjan Chatterjee */
public final class ReflectionStepsDemo {
    public static final class Sample {
        private final String name;
        /** Establishes the instance state used by the reflected method. */
        public Sample(final String name) { this.name = name; }
        /** Returns a greeting from ordinary instance state. */
        public String greet(final String prefix) { return prefix + " " + name; }
        /** Parses an input so invocation can also demonstrate target failure. */
        public int parse(final String text) { return Integer.parseInt(text); }
    }

    /** Demonstrates discovery, invocation and three distinct error categories. */
    public static void main(final String[] args) throws ReflectiveOperationException {
        final Class<Sample> type = Sample.class;
        final Constructor<Sample> constructor = type.getConstructor(String.class);
        final Sample receiver = constructor.newInstance("Mira");
        final Method greet = type.getMethod("greet", String.class);
        System.out.println(type.getSimpleName());
        System.out.println(greet.invoke(receiver, "Hello"));
        try { type.getMethod("missing"); }
        catch (final NoSuchMethodException expected) { System.out.println("missing member"); }
        try { greet.invoke(receiver, 42); }
        catch (final IllegalArgumentException expected) { System.out.println("wrong argument type"); }
        final Method parse = type.getMethod("parse", String.class);
        try { parse.invoke(receiver, "oops"); }
        catch (final InvocationTargetException expected) { System.out.println("target cause=" + expected.getCause().getClass().getSimpleName()); }
    }
}
