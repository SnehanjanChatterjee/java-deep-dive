import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Relates two method type parameters through a named transformation contract. @author Snehanjan Chatterjee */
public final class GenericTransform {
    public interface Transformation<F, T> { T apply(F value); }
    public static final class StringLength implements Transformation<String, Integer> {
        /** Rejects null text and returns its UTF-16 length. */
        @Override public Integer apply(final String value) { return Objects.requireNonNull(value, "value").length(); }
    }
    /** Maps every array element in encounter order without exposing a partially constructed result. */
    public static <F, T> List<T> transform(final F[] values, final Transformation<F, T> operation) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(operation, "operation");
        final List<T> result = new ArrayList<>(values.length);
        for (final F value : values) { result.add(operation.apply(value)); }
        return result;
    }
    /** Demonstrates inferred and explicit arguments for a String-to-Integer transformation. */
    public static void main(final String[] args) {
        final String[] values = {"Java", "JVM"};
        final List<Integer> lengths = transform(values, new StringLength());
        final List<Integer> explicit = GenericTransform.<String, Integer>transform(values, new StringLength());
        System.out.println(lengths);
        System.out.println(explicit);
        System.out.println(Arrays.toString(values));
    }
}
