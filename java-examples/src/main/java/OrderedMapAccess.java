import java.util.LinkedHashMap;
import java.util.Map;

/** Observes accesses through supported methods rather than guessed read/write names. @author Snehanjan Chatterjee */
public final class OrderedMapAccess {
    /** Prints the least-to-most-recent order after conditional updates and bulk insertion. */
    public static void main(final String[] args) {
        final LinkedHashMap<String, Integer> values = new LinkedHashMap<>(16, 0.75f, true);
        values.put("A", 1); values.put("B", 2); values.put("C", 3);
        values.containsKey("A");
        System.out.println("containsKey=" + values.keySet());
        values.getOrDefault("B", 99);
        System.out.println("getOrDefault=" + values.keySet());
        values.replace("A", 99, 100);
        System.out.println("failed replace=" + values.keySet());
        values.replace("A", 1, 10);
        System.out.println("successful replace=" + values.keySet());
        final Map<String, Integer> updates = new LinkedHashMap<>(); updates.put("C", 30); updates.put("D", 4);
        values.putAll(updates);
        System.out.println("putAll=" + values.keySet());
        for (final Map.Entry<String, Integer> entry : values.entrySet()) {
            if (entry.getKey().equals("C")) { entry.setValue(300); }
        }
        System.out.println("entry setValue=" + values.keySet());
    }
}
