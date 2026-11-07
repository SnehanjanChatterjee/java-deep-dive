/** Passes one request context explicitly through an ordinary call chain. @author Snehanjan Chatterjee */
public final class ContextParametersLesson {
    /** Forwards the context that the final formatter needs. */
    private static String report(final String request) { return format(request); }
    /** Formats a value using the explicitly supplied request. */
    private static String format(final String request) { return request + ": report"; }
    /** Demonstrates the default, explicit context-passing mechanism. */
    public static void main(final String[] args) { System.out.println(report("request-42")); }
}
