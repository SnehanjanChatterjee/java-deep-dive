import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Traces callback invocation and removal semantics for nullable HashMap values. @author Snehanjan Chatterjee */
public final class MapCallbacks {
    /** Uses observable callbacks without mutating the same map reentrantly. */
    public static void main(final String[] args) {
        final Map<String, Integer> values = new HashMap<>();
        final List<String> calls = new ArrayList<>();
        values.put("A", null);
        values.computeIfPresent("A", (key, old) -> { calls.add("present"); return old + 1; });
        values.computeIfAbsent("A", key -> { calls.add("absent"); return 3; });
        values.computeIfAbsent("A", key -> { calls.add("skipped"); return 9; });
        values.computeIfPresent("A", (key, old) -> { calls.add("present"); return old + 1; });
        System.out.println("A=" + values.get("A") + ", calls=" + calls);
        values.compute("A", (key, old) -> null);
        System.out.println("compute removed=" + !values.containsKey("A"));
        values.merge("A", 5, (old, incoming) -> { calls.add("merge"); return old + incoming; });
        values.merge("A", 2, (old, incoming) -> { calls.add("merge"); return old + incoming; });
        System.out.println("merged A=" + values.get("A") + ", calls=" + calls);
        values.merge("A", 1, (old, incoming) -> null);
        System.out.println("merge removed=" + !values.containsKey("A"));
        values.put("B", null);
        values.computeIfAbsent("B", key -> null);
        System.out.println("null mapping retained=" + values.containsKey("B"));
        values.put("C", 2);
        values.replaceAll((key, old) -> old == null ? 0 : old * 2);
        final int[] total = {0};
        values.forEach((key, value) -> total[0] += value);
        System.out.println("replaceAll total=" + total[0]);
    }
}
