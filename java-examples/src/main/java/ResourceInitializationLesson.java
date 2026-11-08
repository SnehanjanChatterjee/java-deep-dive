/** Makes a second-resource initialization failure and cleanup suppression observable. */
public final class ResourceInitializationLesson {
    private static final class Resource implements AutoCloseable {
        private final String name;
        private final StringBuilder events;
        private final boolean failClose;
        /** Logs opening, optionally failing before initialization completes. */
        Resource(final String name, final StringBuilder events, final boolean failOpen, final boolean failClose) {
            this.name = name;
            this.events = events;
            this.failClose = failClose;
            events.append("open ").append(name).append('\n');
            if (failOpen) { throw new IllegalStateException("open " + name); }
        }
        /** Logs cleanup and optionally fails after recording the attempt. */
        @Override public void close() {
            events.append("close ").append(name).append('\n');
            if (failClose) { throw new IllegalStateException("close " + name); }
        }
    }
    /** Returns the actual event trace when B's opening fails. */
    public static String trace(final boolean failFirstClose) {
        final StringBuilder events = new StringBuilder();
        try (Resource first = new Resource("A", events, false, failFirstClose);
             Resource second = new Resource("B", events, true, false)) {
            events.append("body\n");
        } catch (final IllegalStateException failure) {
            events.append("primary=").append(failure.getMessage()).append('\n');
            for (final Throwable suppressed : failure.getSuppressed()) {
                events.append("suppressed=").append(suppressed.getMessage()).append('\n');
            }
        }
        return events.toString();
    }
    /** Prints the simultaneous opening and cleanup failure case. */
    public static void main(final String[] args) { System.out.print(trace(true)); }
}
