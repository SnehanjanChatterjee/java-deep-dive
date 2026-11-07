import java.lang.reflect.Method;

/** Inspect metadata and design explicit extension points. @author Snehanjan Chatterjee */
public final class ReflectionExample {
    public static final class Greeter {
        /** Returns a greeting for the supplied name. */
        public String greet(final String name) { return "Hello " + name; }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Method method = Greeter.class.getMethod("greet", String.class);
        System.out.println(method.invoke(new Greeter(), "Mira"));
    }
}
