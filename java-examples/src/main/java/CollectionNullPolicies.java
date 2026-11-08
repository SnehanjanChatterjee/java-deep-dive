import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/** Observes insertion policies without inferring them from interface names. @author Snehanjan Chatterjee */
public final class CollectionNullPolicies {
    /** Attempts one null insertion into the supplied collection. */
    public static boolean acceptsNull(final Collection<String> values) {
        Objects.requireNonNull(values, "values");
        try { values.add(null); return true; }
        catch (final NullPointerException rejected) { return false; }
    }
    /** Attempts a null key or null value independently in the supplied map. */
    public static boolean acceptsNull(final Map<String, Integer> values, final boolean nullKey) {
        Objects.requireNonNull(values, "values");
        final String key = nullKey ? null : "key";
        final Integer value = nullKey ? Integer.valueOf(1) : null;
        try { values.put(key, value); return true; }
        catch (final NullPointerException rejected) { return false; }
    }
    /** Shows common collection policies and both sides of a map mapping. */
    public static void main(final String[] args) {
        System.out.println("ArrayList=" + acceptsNull(new ArrayList<String>()));
        System.out.println("LinkedList=" + acceptsNull(new LinkedList<String>()));
        System.out.println("HashSet=" + acceptsNull(new HashSet<String>()));
        System.out.println("TreeSet natural=" + acceptsNull(new TreeSet<String>()));
        final Comparator<String> nullOrder = Comparator.nullsFirst(Comparator.<String>naturalOrder());
        System.out.println("TreeSet null-order=" + acceptsNull(new TreeSet<String>(nullOrder)));
        System.out.println("ArrayDeque=" + acceptsNull(new ArrayDeque<String>()));
        System.out.println("PriorityQueue=" + acceptsNull(new PriorityQueue<String>()));
        System.out.println("HashMap key/value=" + acceptsNull(new HashMap<String, Integer>(), true)
                + "/" + acceptsNull(new HashMap<String, Integer>(), false));
        System.out.println("LinkedHashMap key/value=" + acceptsNull(new LinkedHashMap<String, Integer>(), true)
                + "/" + acceptsNull(new LinkedHashMap<String, Integer>(), false));
        System.out.println("TreeMap key/value=" + acceptsNull(new TreeMap<String, Integer>(), true)
                + "/" + acceptsNull(new TreeMap<String, Integer>(), false));
        System.out.println("Hashtable key/value=" + acceptsNull(new Hashtable<String, Integer>(), true)
                + "/" + acceptsNull(new Hashtable<String, Integer>(), false));
        System.out.println("ConcurrentHashMap key/value=" + acceptsNull(new ConcurrentHashMap<String, Integer>(), true)
                + "/" + acceptsNull(new ConcurrentHashMap<String, Integer>(), false));
        System.out.println("ConcurrentSkipListMap key/value=" + acceptsNull(new ConcurrentSkipListMap<String, Integer>(), true)
                + "/" + acceptsNull(new ConcurrentSkipListMap<String, Integer>(), false));
        final TreeMap<String, Integer> nullable = new TreeMap<>(nullOrder);
        nullable.put(null, 1);
        System.out.println("floor null key=" + nullable.floorKey("A") + ", entry exists=" + (nullable.floorEntry("A") != null));
    }
}
