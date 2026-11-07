/** Traces primary and suppressed failures with two owned resources. @author Snehanjan Chatterjee */
public final class ResourceFailureTrace {
    private static final class Resource implements AutoCloseable {
        private final String name;
        /** Creates a resource that fails when closed. */
        private Resource(final String name) { this.name = name; }
        /** Reports the close failure to the language-generated suppression logic. */
        @Override public void close() { throw new IllegalStateException("close " + name); }
    }

    /** Prints the selected primary exception followed by its suppressed exceptions. */
    private static void run(final boolean failBody) {
        try (final Resource first = new Resource("A"); final Resource second = new Resource("B")) {
            if (failBody) { throw new IllegalStateException("body"); }
        } catch (final IllegalStateException failure) {
            System.out.println(failure.getMessage());
            for (final Throwable suppressed : failure.getSuppressed()) {
                System.out.println(suppressed.getMessage());
            }
        }
    }

    /** Shows body-failure and close-only failure paths. */
    public static void main(final String[] args) {
        run(true);
        run(false);
    }
}
