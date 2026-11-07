package study.client;
import study.api.Greeter;
/** Uses an API through a declared module dependency. @author Snehanjan Chatterjee */
public final class Main {
    /** Executes the module client. */
    public static void main(final String[] args) { System.out.println(Greeter.greet("Mira")); }
}
