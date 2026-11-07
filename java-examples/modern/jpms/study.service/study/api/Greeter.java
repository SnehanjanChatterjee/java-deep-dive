package study.api;
/** Public API exported by study.service. @author Snehanjan Chatterjee */
public final class Greeter {
    /** Returns a greeting without exposing implementation packages. */
    public static String greet(final String name) { return "Hello " + name; }
}
