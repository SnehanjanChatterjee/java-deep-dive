/*
 * Java Deep Dive educational examples.
 * Author: Snehanjan Chatterjee.
 */
import java.util.Map;

/** Demonstrates collisions, replacement, nulls and backed entries. @author Snehanjan Chatterjee */
public final class HashMapDemo {
    /** Runs a deterministic example without depending on iteration order. */
    public static void main(final String[] args) {
        final Map<Integer, String> map = new LearningHashMap<>();
        map.put(1, "one");
        map.put(17, "seventeen");
        map.put(33, "thirty-three");
        System.out.println(map.get(17));
        System.out.println(map.put(17, "updated"));
        System.out.println(map.get(17));
        System.out.println(map.remove(1));
        map.put(null, null);
        System.out.println(map.containsKey(null));
        System.out.println(map.size());
    }
}
