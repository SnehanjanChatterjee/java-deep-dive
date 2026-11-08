import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;

/** Verifies bounded algorithms and capture without unchecked casts. @author Snehanjan Chatterjee */
public final class GenericAlgorithmsTest {
    private static class Parent implements Comparable<Parent> {
        private final int rank;
        private Parent(final int rank) { this.rank = rank; }
        @Override public int compareTo(final Parent other) { return Integer.compare(rank, other.rank); }
    }
    private static final class Child extends Parent {
        private Child(final int rank) { super(rank); }
    }
    @Test public void testCopySwapAndMaximum() {
        final List<Integer> values = new ArrayList<>(Arrays.asList(1, 3, 2));
        GenericAlgorithms.swap(values, 0, 1); assertEquals(Arrays.asList(3, 1, 2), values);
        final List<Number> destination = new ArrayList<>(); GenericAlgorithms.copy(destination, values);
        assertEquals(values, destination); assertEquals(Integer.valueOf(3), GenericAlgorithms.max(values));
        assertEquals(Integer.valueOf(-1), GenericAlgorithms.max(Arrays.asList(-3, -1, -2)));
    }
    @Test public void testComparisonInheritedFromSupertype() {
        final Child first = new Child(1);
        final Child maximum = new Child(2);
        final Child result = GenericAlgorithms.max(Arrays.asList(first, maximum));
        assertSame(maximum, result);
    }
    @Test public void testEmptyNullAndInvalidIndex() {
        assertThrows(NoSuchElementException.class, () -> GenericAlgorithms.max(Collections.<Integer>emptyList()));
        assertThrows(NullPointerException.class, () -> GenericAlgorithms.max(null));
        assertThrows(NullPointerException.class, () -> GenericAlgorithms.max(Arrays.asList(1, null)));
        assertThrows(NullPointerException.class, () -> GenericAlgorithms.copy(null, Arrays.asList(1)));
        assertThrows(NullPointerException.class, () -> GenericAlgorithms.copy(new ArrayList<Integer>(), null));
        assertThrows(NullPointerException.class, () -> GenericAlgorithms.swap(null, 0, 1));
        final List<Integer> values = new ArrayList<>(Arrays.asList(1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> GenericAlgorithms.swap(values, 0, 2));
        assertEquals(Arrays.asList(1, 2), values);
    }
}
