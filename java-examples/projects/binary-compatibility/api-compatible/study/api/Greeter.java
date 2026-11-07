package study.api;

/** Keeps the old descriptor while adding a broader overload. @author Snehanjan Chatterjee */
public final class Greeter {
    /** Retains the signature already compiled clients require. */
    public String greet(final String name) { return greet((CharSequence) name); }
    /** Supplies the broader implementation used by both entry points. */
    public String greet(final CharSequence name) { return "Welcome " + name; }
}
