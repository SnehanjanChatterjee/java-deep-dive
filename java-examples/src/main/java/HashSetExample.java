import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

/** Demonstrates HashSet and LinkedHashSet contracts. @author Snehanjan Chatterjee */
public final class HashSetExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final Set<String> unordered = new HashSet<>();
        unordered.add("Aa"); unordered.add("BB");
        System.out.println(unordered.size());
        System.out.println(unordered.add(new String("Aa")));
        final Set<String> ordered = new LinkedHashSet<>(Arrays.asList("B", "A", "B", "C"));
        System.out.println(ordered);
        ordered.remove("A"); ordered.add("A");
        System.out.println(ordered);
    }
}
