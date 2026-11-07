package course.app;

import course.spi.Formatter;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceLoader;

/** Separates provider discovery from an explicit unique-capability policy. @author Snehanjan Chatterjee */
public final class Discover {
    /** Selects exactly one matching provider, preserving discovery/configuration failures. */
    public static Formatter select(final Iterable<Formatter> providers, final String wanted) {
        Objects.requireNonNull(providers, "providers");
        Objects.requireNonNull(wanted, "wanted");
        Formatter selected = null;
        for (final Formatter provider : providers) {
            Objects.requireNonNull(provider, "provider");
            if (wanted.equals(Objects.requireNonNull(provider.id(), "provider id"))) {
                if (selected != null) { throw new IllegalStateException("duplicate capability: " + wanted); }
                selected = provider;
            }
        }
        if (selected == null) { throw new NoSuchElementException("no provider for: " + wanted); }
        return selected;
    }

    /** Discovers configured providers without importing either provider implementation. */
    public static void main(final String[] args) {
        final Formatter selected = select(ServiceLoader.load(Formatter.class), "upper");
        System.out.println(selected.format("Java"));
    }
}
