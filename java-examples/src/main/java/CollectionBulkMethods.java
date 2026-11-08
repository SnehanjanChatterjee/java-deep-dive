import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Makes shared Collection operations and their result meanings observable. @author Snehanjan Chatterjee */
public final class CollectionBulkMethods {
    /** Shows membership-based bulk removal, retention and typed array conversion. */
    public static void main(final String[] args) {
        final Collection<String> names = new ArrayList<>();
        System.out.println(names.addAll(Arrays.asList("A", "B", "A", "C")));
        System.out.println(names.containsAll(Arrays.asList("A", "A", "B")));
        System.out.println(names.removeAll(Collections.singleton("A")));
        System.out.println(names);
        names.retainAll(Arrays.asList("B", "missing"));
        System.out.println(names);
        final String[] oversized = {"old", "old", "tail"};
        final String[] result = names.toArray(oversized);
        System.out.println((result == oversized) + ":" + Arrays.toString(result));
        final List<Integer> reserved = new ArrayList<>(10);
        System.out.println("reserved size=" + reserved.size());
        names.clear();
        System.out.println(names.isEmpty());
    }
}
