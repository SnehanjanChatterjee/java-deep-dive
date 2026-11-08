import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Separates conditional updates and backed views from detached entry values. @author Snehanjan Chatterjee */
public final class MapViewsAndUpdates {
    /** Prints only specified results, avoiding HashMap encounter-order assumptions. */
    public static void main(final String[] args) {
        final Map<String, Integer> map = new HashMap<>();
        map.put("A", null);
        System.out.println(map.getOrDefault("A", 99));
        System.out.println(map.putIfAbsent("A", 7));
        System.out.println(map.replace("A", 7, 8));
        System.out.println(map.remove("A", 7));
        System.out.println(map.get("A"));
        map.put("B", 8);
        final Collection<Integer> values = map.values();
        values.remove(8);
        System.out.println("one removed=" + map.size() + ", still has 8=" + map.containsValue(8));
        final Set<String> keys = map.keySet();
        keys.clear();
        System.out.println("backing empty=" + map.isEmpty());
        map.put("only", 1);
        final Map.Entry<String, Integer> live = map.entrySet().iterator().next();
        final Map.Entry<String, Integer> snapshot = new AbstractMap.SimpleImmutableEntry<>(live);
        live.setValue(9);
        System.out.println("live=" + map.get("only") + ", snapshot=" + snapshot.getValue());
    }
}
