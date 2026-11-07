
/** Preserve failure information and close resources deterministically. @author Snehanjan Chatterjee */
public final class ResourceFailures {
    static final class Resource implements AutoCloseable {
        @Override public void close() { throw new IllegalStateException("close"); }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        try (final Resource resource = new Resource()) {
            throw new IllegalStateException("body");
        } catch (final IllegalStateException failure) {
            System.out.println(failure.getMessage());
            System.out.println(failure.getSuppressed().length);
        }
    }
}
