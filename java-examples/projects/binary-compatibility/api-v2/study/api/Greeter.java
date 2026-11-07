package study.api;

/** A source-friendly change that removes the old binary descriptor. @author Snehanjan Chatterjee */
public final class Greeter {
    /** Accepts a broader source argument type under a different binary signature. */
    public String greet(final CharSequence name) { return "Welcome " + name; }
}
