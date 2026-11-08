import java.util.Objects;

/** Shows a stable application-frame view without inventing exact JDK library frames. */
public final class ExceptionTraceLesson {
    /** Parses at the leaf of the application call chain. */
    private static int parseCount(final String text) { return Integer.parseInt(text); }
    /** Adds the application operation around the parser. */
    private static int loadCount(final String text) { return parseCount(text); }
    /** Selects this example's application frames, retaining their captured order. */
    public static String applicationFrames(final Throwable failure) {
        Objects.requireNonNull(failure, "failure");
        final StringBuilder result = new StringBuilder(failure.getClass().getSimpleName());
        for (final StackTraceElement frame : failure.getStackTrace()) {
            if (ExceptionTraceLesson.class.getName().equals(frame.getClassName())) {
                result.append('\n').append(frame.getMethodName());
            }
        }
        return result.toString();
    }
    /** Catches at a teaching boundary and prints the selected captured frames. */
    public static void main(final String[] args) {
        try { System.out.println(loadCount("bad")); }
        catch (final NumberFormatException failure) { System.out.println(applicationFrames(failure)); }
    }
}
