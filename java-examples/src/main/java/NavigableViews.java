import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;

/** Applies neighbor and range operations in each view's comparison order. @author Snehanjan Chatterjee */
public final class NavigableViews {
    /** Compares ascending/descending queries, bounded mutation and entry snapshots. */
    public static void main(final String[] args) {
        final NavigableMap<Integer, String> map = new TreeMap<>();
        map.put(10, "ten"); map.put(20, "twenty"); map.put(30, "thirty");
        System.out.println("ascending floor/ceiling=" + map.floorKey(25) + "/" + map.ceilingKey(25));
        final NavigableMap<Integer, String> descending = map.descendingMap();
        System.out.println("descending floor/ceiling=" + descending.floorKey(25) + "/" + descending.ceilingKey(25));
        final NavigableMap<Integer, String> range = descending.subMap(30, true, 10, false);
        System.out.println(range.keySet());
        final Map.Entry<Integer, String> snapshot = map.floorEntry(20);
        map.put(20, "updated");
        System.out.println("navigation snapshot=" + snapshot.getValue());
        try { snapshot.setValue("invalid"); throw new IllegalStateException("expected immutable entry"); }
        catch (final UnsupportedOperationException expected) { System.out.println("navigation entry update rejected"); }
        try { range.put(10, "invalid"); throw new IllegalStateException("expected range rejection"); }
        catch (final IllegalArgumentException expected) { System.out.println("out-of-range key rejected"); }
        range.remove(20);
        System.out.println("backing keys=" + map.keySet());
        final NavigableSet<Integer> set = new TreeSet<>(Arrays.asList(10, 20, 30));
        System.out.println("descending lower/higher=" + set.descendingSet().lower(20) + "/" + set.descendingSet().higher(20));
        set.subSet(10, true, 30, false).clear();
        System.out.println("set after range clear=" + set);
    }
}
