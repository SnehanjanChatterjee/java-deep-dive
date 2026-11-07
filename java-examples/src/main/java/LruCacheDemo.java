import java.util.LinkedHashMap;
import java.util.Map;

/** Distinguish sorted order, encounter order and eviction policy. @author Snehanjan Chatterjee */
public final class LruCacheDemo {
    static final class Cache<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = 1L; private final int capacity;
        /** Creates a bounded access-order map. */
        Cache(final int capacity) { super(16, 0.75f, true); if (capacity < 1) { throw new IllegalArgumentException("positive capacity required"); } this.capacity = capacity; }
        /** Evicts only when the configured mapping limit is exceeded. */
        @Override protected boolean removeEldestEntry(final Map.Entry<K, V> entry) { return size() > capacity; }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Cache<String, Integer> cache = new Cache<>(2);
        cache.put("A", 1); cache.put("B", 2);
        cache.get("A");
        cache.put("C", 3);
        System.out.println(cache.containsKey("B"));
        System.out.println(cache.get("A"));
    }
}
