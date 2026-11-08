import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/** Reads legacy map and cursor contracts without assuming encounter order. @author Snehanjan Chatterjee */
public final class HashtableLesson {
    /** Copies key references; the caller must prevent structural changes throughout enumeration. */
    public static <K, V> List<K> copyKeys(final Hashtable<K, V> table) {
        Objects.requireNonNull(table, "table");
        final List<K> result = new ArrayList<>();
        final Enumeration<K> keys = table.keys();
        while (keys.hasMoreElements()) { result.add(keys.nextElement()); }
        return result;
    }

    /** Shows replacement, legacy contains, null rejection and collision membership. */
    public static void main(final String[] args) {
        final Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Ada", 10);
        table.put("Lin", 8);
        System.out.println("replaced=" + table.put("Ada", 20));
        System.out.println("Ada=" + table.get("Ada") + ", size=" + table.size());
        System.out.println("key/legacy-value/value=" + table.containsKey("Ada") + "/"
                + table.contains("Ada") + "/" + table.containsValue(20));
        final List<String> keys = copyKeys(table);
        System.out.println("enumerated=" + keys.size() + ", members=" + (keys.contains("Ada") && keys.contains("Lin")));
        System.out.println("missing=" + table.get("missing"));
        try { table.put(null, 1); throw new IllegalStateException("expected null-key rejection"); }
        catch (final NullPointerException expected) { System.out.println("null key rejected"); }
        try { table.put("missing", null); throw new IllegalStateException("expected null-value rejection"); }
        catch (final NullPointerException expected) { System.out.println("null value rejected"); }
        System.out.println("removed=" + table.remove("Lin") + ", size=" + table.size());
        final Hashtable<String, Integer> collisions = new Hashtable<>();
        collisions.put("Aa", 1);
        collisions.put("BB", 2);
        System.out.println("collision members=" + collisions.size());
    }
}
