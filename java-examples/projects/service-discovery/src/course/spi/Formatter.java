package course.spi;

/** Defines a service independently from its provider classes. @author Snehanjan Chatterjee */
public interface Formatter {
    /** Returns the provider's stable capability identifier. */
    String id();
    /** Formats non-null text using the provider's advertised capability. */
    String format(String value);
}
