import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Compares Java 9 factories and Java 10 snapshots with live wrappers. @author Snehanjan Chatterjee */
public final class CollectionFactories {
    /** Makes backing, null rejection, duplicate policy and shallow ownership visible. */
    public static void main(final String[] args) {
        final String[] array = {"A", "B"};
        final List<String> fixed = Arrays.asList(array);
        final List<String> view = Collections.unmodifiableList(fixed);
        final List<String> snapshot = List.copyOf(fixed);
        final List<String> factory = List.of(array);
        array[0] = "changed";
        fixed.set(1, "C");
        System.out.println("array=" + Arrays.toString(array));
        System.out.println("view=" + view);
        System.out.println("snapshot=" + snapshot);
        System.out.println("factory=" + factory);
        final StringBuilder element = new StringBuilder("x");
        final List<StringBuilder> shallow = List.of(element);
        element.append('!');
        System.out.println("mutable element=" + shallow.get(0));
        try { fixed.add("D"); throw new IllegalStateException("expected fixed-size rejection"); }
        catch (final UnsupportedOperationException expected) { System.out.println("fixed-size add rejected"); }
        try { snapshot.set(0, "D"); throw new IllegalStateException("expected unmodifiable rejection"); }
        catch (final UnsupportedOperationException expected) { System.out.println("snapshot set rejected"); }
        try { List.of((String) null); throw new IllegalStateException("expected null rejection"); }
        catch (final NullPointerException expected) { System.out.println("factory null rejected"); }
        try { Set.of("A", "A"); throw new IllegalStateException("expected duplicate rejection"); }
        catch (final IllegalArgumentException expected) { System.out.println("duplicate set rejected"); }
        System.out.println("copyOf duplicate count=" + Set.copyOf(Arrays.asList("A", "A")).size());
        try { Map.of("A", 1, "A", 2); throw new IllegalStateException("expected duplicate key rejection"); }
        catch (final IllegalArgumentException expected) { System.out.println("duplicate map key rejected"); }
        final Map.Entry<String, Integer> nullable = new AbstractMap.SimpleImmutableEntry<>(null, null);
        System.out.println("immutable entry permits null=" + (nullable.getKey() == null));
        try { Map.entry(null, 1); throw new IllegalStateException("expected entry null rejection"); }
        catch (final NullPointerException expected) { System.out.println("Map.entry null rejected"); }
    }
}
