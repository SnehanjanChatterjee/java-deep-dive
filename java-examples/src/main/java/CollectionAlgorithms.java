import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/** Applies Collections algorithms to deliberately chosen mutable sequences. @author Snehanjan Chatterjee */
public final class CollectionAlgorithms {
    /** Inserts into an ascending sorted list using the binary-search insertion point. */
    public static void insertSorted(final List<Integer> values, final int value) {
        Objects.requireNonNull(values, "values");
        final int result = Collections.binarySearch(values, value);
        final int insertion = result >= 0 ? result : -result - 1;
        values.add(insertion, value);
    }
    /** Demonstrates mutation, copying, search results and multiplicity. */
    public static void main(final String[] args) {
        final List<Integer> values = new ArrayList<>(Arrays.asList(4, 1, 3, 1));
        Collections.sort(values);
        System.out.println(values);
        System.out.println("missing 2=" + Collections.binarySearch(values, 2));
        insertSorted(values, 2);
        System.out.println(values);
        Collections.reverse(values);
        Collections.rotate(values, 2);
        System.out.println(values);
        Collections.swap(values, 0, 4);
        System.out.println(values);
        final List<Integer> destination = new ArrayList<>(Collections.nCopies(6, 0));
        Collections.copy(destination, values);
        System.out.println(destination);
        Collections.fill(destination.subList(4, 6), 9);
        Collections.replaceAll(destination, 1, 7);
        System.out.println(destination);
        System.out.println("frequency=" + Collections.frequency(destination, 7));
        System.out.println("disjoint=" + Collections.disjoint(destination, Arrays.asList(2, 3)));
        System.out.println("min/max=" + Collections.min(destination) + "/" + Collections.max(destination));
        System.out.println("sublist=" + Collections.indexOfSubList(destination, Arrays.asList(3, 2)));
        final List<Integer> shuffled = new ArrayList<>(values);
        Collections.shuffle(shuffled, new Random(7L));
        Collections.sort(shuffled);
        System.out.println("shuffle preserves values=" + shuffled.equals(Arrays.asList(1, 1, 2, 3, 4)));
    }
}
