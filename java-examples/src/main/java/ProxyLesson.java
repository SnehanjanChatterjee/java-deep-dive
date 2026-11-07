import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Objects;

/** Implements one interface boundary while preserving identity and target failures. @author Snehanjan Chatterjee */
public final class ProxyLesson {
    public interface Greeting { String greet(String name) throws IOException; }

    /** Creates an interface proxy with explicit Object-method behavior. */
    public static Greeting wrap(final Greeting target) {
        Objects.requireNonNull(target, "target");
        return (Greeting) Proxy.newProxyInstance(ProxyLesson.class.getClassLoader(), new Class<?>[] {Greeting.class}, (proxy, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                switch (method.getName()) {
                    case "equals": return proxy == args[0];
                    case "hashCode": return System.identityHashCode(proxy);
                    case "toString": return "Greeting proxy";
                    default: throw new IllegalStateException("unsupported Object method");
                }
            }
            try { return method.invoke(target, args); }
            catch (final InvocationTargetException failure) { throw failure.getCause(); }
        });
    }

    /** Shows delegation, reflexive equality and an unwrapped checked failure. */
    public static void main(final String[] args) throws IOException {
        final Greeting target = name -> { if (name.isEmpty()) { throw new IOException("empty name"); } return "Hello " + name; };
        final Greeting proxy = wrap(target);
        System.out.println(proxy.greet("Mira"));
        System.out.println(proxy.equals(proxy));
        System.out.println(proxy.equals(target));
        try { proxy.greet(""); }
        catch (final IOException expected) { System.out.println("cause preserved"); }
    }
}
