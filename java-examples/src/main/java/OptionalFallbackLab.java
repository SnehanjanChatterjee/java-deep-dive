import java.util.Optional;

/** Makes eager argument evaluation and lazy fallback invocation observable. @author Snehanjan Chatterjee */
public final class OptionalFallbackLab {
    private static final class Counter { private int calls; }
    public static final class Observation {
        private final String value;
        private final int calls;
        private Observation(final String value, final int calls) { this.value = value; this.calls = calls; }
        /** Returns the selected value. */
        public String value() { return value; }
        /** Returns how often the fallback body ran. */
        public int calls() { return calls; }
    }
    /** Records the fallback evaluation and returns its value. */
    private static String fallback(final Counter counter) { counter.calls++; return "fallback"; }
    /** Compares evaluation policies using independent counters for each experiment. */
    public static Observation observe(final boolean present, final boolean lazy) {
        final Counter counter = new Counter();
        final Optional<String> optional = present ? Optional.of("present") : Optional.empty();
        final String value = lazy ? optional.orElseGet(() -> fallback(counter)) : optional.orElse(fallback(counter));
        return new Observation(value, counter.calls);
    }
    /** Prints both presence cases and both fallback strategies. */
    public static void main(final String[] args) {
        for (final boolean present : new boolean[] {true, false}) {
            for (final boolean lazy : new boolean[] {false, true}) {
                final Observation observation = observe(present, lazy);
                System.out.println((present ? "present" : "empty") + " " + (lazy ? "lazy" : "eager")
                        + ": " + observation.value() + ", calls=" + observation.calls());
            }
        }
    }
}
