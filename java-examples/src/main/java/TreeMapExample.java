import java.util.TreeMap;
import java.util.NavigableMap;

/** Demonstrates TreeMap contracts. @author Snehanjan Chatterjee */
public final class TreeMapExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final NavigableMap<Integer, String> schedule = new TreeMap<>();
        schedule.put(10, "A"); schedule.put(30, "C"); schedule.put(20, "B");
        System.out.println(schedule.keySet());
        System.out.println(schedule.floorKey(25));
        System.out.println(schedule.ceilingKey(25));
        final NavigableMap<Integer, String> range = schedule.subMap(10, true, 30, false);
        range.remove(20);
        System.out.println(schedule.keySet());
    }
}
