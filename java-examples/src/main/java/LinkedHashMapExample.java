import java.util.Map;
import java.util.LinkedHashMap;

/** Demonstrates LinkedHashMap contracts. @author Snehanjan Chatterjee */
public final class LinkedHashMapExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final Map<String, Integer> insertion = new LinkedHashMap<>();
        insertion.put("A", 1); insertion.put("B", 2); insertion.put("A", 3);
        System.out.println(insertion.keySet());
        final LinkedHashMap<String, Integer> access = new LinkedHashMap<>(16, 0.75f, true);
        access.put("A", 1); access.put("B", 2); access.put("C", 3);
        access.get("A");
        System.out.println(access.keySet());
        access.containsKey("B");
        System.out.println(access.keySet());
    }
}
