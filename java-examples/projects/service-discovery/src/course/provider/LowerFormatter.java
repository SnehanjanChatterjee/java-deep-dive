package course.provider;

import course.spi.Formatter;
import java.util.Locale;

/** Supplies a distinct formatting capability. @author Snehanjan Chatterjee */
public final class LowerFormatter implements Formatter {
    /** Provides the public no-argument construction required by this classpath service. */
    public LowerFormatter() { }
    /** Advertises the lowercase capability. */
    @Override public String id() { return "lower"; }
    /** Uses locale-independent case conversion for this demonstration. */
    @Override public String format(final String value) { return value.toLowerCase(Locale.ROOT); }
}
