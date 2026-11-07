
/** Separate reproducible builds, dependency resolution and API evolution. @author Snehanjan Chatterjee */
public final class CompatibleApi {
    /** Preserves the original greeting behavior. */
    static String greet(final String name) { return greet(name, "Hello"); }
    /** Uses an explicit greeting prefix. */
    static String greet(final String name, final String prefix) { return prefix + " " + name; }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        System.out.println(greet("Mira"));
        System.out.println(greet("Mira", "Welcome"));
    }
}
