import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Builds independent rows and distinguishes outer, row and element ownership. @author Snehanjan Chatterjee */
public final class NestedCollections {
    /** Creates a rectangular list whose rows can be mutated independently. */
    public static List<List<Integer>> matrix(final int rows, final int columns, final int initial) {
        if (rows < 0 || columns < 0) { throw new IllegalArgumentException("negative dimension"); }
        final List<List<Integer>> result = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            result.add(new ArrayList<>(Collections.nCopies(columns, initial)));
        }
        return result;
    }
    /** Copies each row while sharing immutable Integer elements; null rows are rejected. */
    public static List<List<Integer>> copyRows(final List<? extends List<Integer>> source) {
        Objects.requireNonNull(source, "source");
        final List<List<Integer>> result = new ArrayList<>(source.size());
        for (final List<Integer> row : source) {
            result.add(new ArrayList<>(Objects.requireNonNull(row, "row")));
        }
        return result;
    }
    /** Snapshots String-set rows before using them as hash-set members. */
    public static Set<Set<String>> freezeDistinctRows(final List<? extends Set<String>> source) {
        Objects.requireNonNull(source, "source");
        final Set<Set<String>> result = new LinkedHashSet<>();
        for (final Set<String> row : source) {
            result.add(Collections.unmodifiableSet(new LinkedHashSet<>(Objects.requireNonNull(row, "row"))));
        }
        return Collections.unmodifiableSet(result);
    }
    /** Traces replacement, row insertion and a deliberately shared-row mistake. */
    public static void main(final String[] args) {
        final List<List<Integer>> grid = matrix(2, 3, 0);
        grid.get(0).set(1, 17);
        grid.get(1).add(9);
        System.out.println(grid);
        final List<List<Integer>> outerCopy = new ArrayList<>(grid);
        final List<List<Integer>> rowCopy = copyRows(grid);
        grid.get(0).set(0, 5);
        System.out.println(outerCopy);
        System.out.println(rowCopy);
        final List<Integer> sharedRow = new ArrayList<>(Arrays.asList(0, 0));
        final List<List<Integer>> aliases = new ArrayList<>(Collections.nCopies(2, sharedRow));
        aliases.get(0).set(0, 8);
        System.out.println(aliases);
        final Set<String> row = new LinkedHashSet<>(Arrays.asList("A", "B"));
        final Set<Set<String>> distinct = freezeDistinctRows(Arrays.asList(row,
                new LinkedHashSet<>(Arrays.asList("B", "A"))));
        row.add("C");
        System.out.println("distinct frozen rows=" + distinct);
    }
}
