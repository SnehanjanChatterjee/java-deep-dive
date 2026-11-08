/** Makes the ordinary try-with-resources call order visible without external I/O. @author Snehanjan Chatterjee */
public final class ResourceScopeLesson {
    private static final class TraceResource implements AutoCloseable {
        /** Records successful construction of the teaching resource. */
        private TraceResource() { System.out.println("opened"); }

        /** Records work performed inside the owning scope. */
        private void use() { System.out.println("used"); }

        /** Records the language's cleanup invocation; no operating-system resource is held. */
        @Override public void close() { System.out.println("closed"); }
    }

    /** Uses one resource and resumes only after its automatic close call. */
    public static void main(final String[] args) {
        try (final TraceResource resource = new TraceResource()) {
            resource.use();
        }
        System.out.println("after scope");
    }
}
