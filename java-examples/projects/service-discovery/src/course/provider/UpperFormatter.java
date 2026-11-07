package course.provider;

import course.spi.Formatter;
import java.util.Locale;

/** Supplies an uppercase-formatting service provider. @author Snehanjan Chatterjee */
public final class UpperFormatter implements Formatter {
    /** Provides the public no-argument construction required by this classpath service. */
    public UpperFormatter() { }
    /** Advertises the uppercase capability. */
    @Override public String id() { return "upper"; }
    /** Uses locale-independent case conversion for this demonstration. */
    @Override public String format(final String value) { return value.toUpperCase(Locale.ROOT); }
}
