import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/** Implements producer/consumer bounds and wildcard capture. @author Snehanjan Chatterjee */
public final class GenericAlgorithms {
    /** Appends source values to a destination accepting their common type. */
    public static <T> void copy(final List<? super T> destination, final List<? extends T> source) {
        Objects.requireNonNull(destination, "destination");
        for (final T value : Objects.requireNonNull(source, "source")) { destination.add(value); }
    }

    /** Returns a maximum of a nonempty, non-null sequence of non-null comparable values. */
    public static <T extends Comparable<? super T>> T max(final List<? extends T> values) {
        final Iterator<? extends T> iterator = Objects.requireNonNull(values, "values").iterator();
        if (!iterator.hasNext()) { throw new NoSuchElementException("empty sequence"); }
        T best = Objects.requireNonNull(iterator.next(), "element");
        while (iterator.hasNext()) {
            final T candidate = Objects.requireNonNull(iterator.next(), "element");
            if (candidate.compareTo(best) > 0) { best = candidate; }
        }
        return best;
    }

    /** Swaps two positions without exposing the list's unknown element type. */
    public static void swap(final List<?> values, final int left, final int right) {
        swapCaptured(Objects.requireNonNull(values, "values"), left, right);
    }

    /** Names one captured type so a read value can be written back safely. */
    private static <T> void swapCaptured(final List<T> values, final int left, final int right) {
        final T first = values.get(left);
        final T second = values.get(right);
        values.set(left, second);
        values.set(right, first);
    }

    /** Runs the bounds and capture examples. */
    public static void main(final String[] args) {
        final List<Integer> values = new ArrayList<>(Arrays.asList(2, 1, 3));
        swap(values, 0, 2);
        final List<Number> destination = new ArrayList<>();
        copy(destination, values);
        System.out.println(values);
        System.out.println(max(values));
        System.out.println(destination);
    }
}
