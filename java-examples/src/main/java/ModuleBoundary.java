import java.util.Locale;

/** Separate dependency readability, reflective access and runtime packaging. @author Snehanjan Chatterjee */
public final class ModuleBoundary {
    interface Formatter { String format(String value); }
    static final class PlainFormatter implements Formatter {
        @Override public String format(final String value) { return value.toUpperCase(Locale.ROOT); }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Formatter formatter = new PlainFormatter();
        System.out.println(formatter.format("java"));
    }
}
