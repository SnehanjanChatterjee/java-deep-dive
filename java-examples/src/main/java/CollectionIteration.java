import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/** Demonstrates traversal, supported removal and a backed range. @author Snehanjan Chatterjee */
public final class CollectionIteration {
    /** Removes short names through the iterator that owns traversal. */
    public static void removeShortNames(final List<String> names, final int minimumLength) {
        Objects.requireNonNull(names, "names");
        if (minimumLength < 0) { throw new IllegalArgumentException("negative minimum length"); }
        final Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            final String name = iterator.next();
            if (name.length() < minimumLength) { iterator.remove(); }
        }
    }

    /** Runs a small traversal whose output does not depend on hash ordering. */
    public static void main(final String[] args) {
        final List<String> names = new ArrayList<>(Arrays.asList("Al", "Mira", "Bo", "Noor"));
        removeShortNames(names, 3);
        for (final String name : names) { System.out.println(name); }
        final List<String> view = names.subList(0, 1);
        final List<String> copy = new ArrayList<>(view);
        view.set(0, "Ada");
        System.out.println(names);
        System.out.println(copy);
    }
}
