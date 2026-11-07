package study.api;

/** First version of a small published method contract. @author Snehanjan Chatterjee */
public final class Greeter {
    /** Greets the caller using the String descriptor recorded by the client. */
    public String greet(final String name) { return "Hello " + name; }
}
