import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.ServiceLoader;

/** Compares metadata, invocation and provider discovery using one service. @author Snehanjan Chatterjee */
public final class MetadataLab {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Label { String value(); }

    public interface Greeter { String greet(String name); }

    /** Public no-argument provider, discoverable by ServiceLoader. */
    public static final class PlainGreeter implements Greeter {
        /** Returns a deterministic greeting. */
        @Label("greeting")
        @Override public String greet(final String name) { return "Hello " + name; }
    }

    /** Discovers the existing provider class through a temporary service resource. */
    private static String discover(final String name) throws IOException {
        final Path root = Files.createTempDirectory("study-services-");
        final Path metadata = root.resolve("META-INF");
        final Path services = metadata.resolve("services");
        final Path descriptor = services.resolve(Greeter.class.getName());
        try {
            Files.createDirectories(services);
            Files.write(descriptor, Collections.singletonList(PlainGreeter.class.getName()), StandardCharsets.UTF_8);
            try (final URLClassLoader loader = new URLClassLoader(new URL[] {root.toUri().toURL()},
                    MetadataLab.class.getClassLoader())) {
                for (final Greeter provider : ServiceLoader.load(Greeter.class, loader)) {
                    return provider.greet(name);
                }
                throw new IllegalStateException("no Greeter provider discovered");
            }
        } finally {
            Files.deleteIfExists(descriptor);
            Files.deleteIfExists(services);
            Files.deleteIfExists(metadata);
            Files.deleteIfExists(root);
        }
    }

    /** Creates a proxy with identity Object methods and unwrapped service failures. */
    public static Greeter createProxy(final Greeter target) {
        Objects.requireNonNull(target, "target");
        return (Greeter) Proxy.newProxyInstance(MetadataLab.class.getClassLoader(),
                new Class<?>[] {Greeter.class}, (instance, invoked, arguments) -> {
                    if (invoked.getDeclaringClass() == Object.class) {
                        switch (invoked.getName()) {
                            case "equals": return instance == arguments[0];
                            case "hashCode": return System.identityHashCode(instance);
                            case "toString": return "GreeterProxy(" + target.getClass().getName() + ")";
                            default: throw new IllegalStateException("unsupported Object method");
                        }
                    }
                    try { return invoked.invoke(target, arguments); }
                    catch (final InvocationTargetException failure) { throw failure.getCause(); }
                });
    }

    /** Exercises four invocation/discovery mechanisms over the same service contract. */
    public static void main(final String[] args) throws Throwable {
        final PlainGreeter target = new PlainGreeter();
        final Method method = PlainGreeter.class.getMethod("greet", String.class);
        System.out.println(method.isAnnotationPresent(Label.class));
        System.out.println(method.invoke(target, "Mira"));
        final Greeter proxy = createProxy(target);
        System.out.println(proxy.greet("Mira"));
        final MethodHandle handle = MethodHandles.lookup().findVirtual(PlainGreeter.class, "greet",
                MethodType.methodType(String.class, String.class));
        System.out.println((String) handle.invokeExact(target, "Mira"));
        System.out.println(discover("Mira"));
    }
}
