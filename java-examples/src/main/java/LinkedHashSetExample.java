import java.util.Arrays;
import java.util.Set;
import java.util.LinkedHashSet;

/** Demonstrates LinkedHashSet contracts. @author Snehanjan Chatterjee */
public final class LinkedHashSetExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final Set<String> values = new LinkedHashSet<>(Arrays.asList("B", "A", "B", "C"));
        System.out.println(values);
        System.out.println(values.add("A"));
        System.out.println(values);
        values.remove("A"); values.add("A");
        System.out.println(values);
    }
}
