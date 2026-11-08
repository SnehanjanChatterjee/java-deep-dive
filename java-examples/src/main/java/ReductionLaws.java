import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Connects the accumulator and combiner of a three-argument reduction. @author Snehanjan Chatterjee */
public final class ReductionLaws {
    /** Adds UTF-16 lengths with exact long arithmetic; null text is an explicit failure. */
    public static long totalLength(final List<String> values) {
        Objects.requireNonNull(values, "values");
        return values.stream().reduce(0L,
                (total, text) -> Math.addExact(total, Objects.requireNonNull(text, "text").length()), Math::addExact);
    }
    /** Contrasts lawful partition meaning with an invalid sum identity. */
    public static void main(final String[] args) {
        final List<String> words = Arrays.asList("Java", "JVM", "a");
        System.out.println("length=" + totalLength(words));
        System.out.println("empty length=" + totalLength(Collections.<String>emptyList()));
        final long left = totalLength(words.subList(0, 1));
        final long right = totalLength(words.subList(1, 3));
        System.out.println("combined=" + Math.addExact(left, right));
        final int invalidSequential = Arrays.asList(1, 2, 3, 4).stream().reduce(1, Integer::sum);
        final int invalidPartitions = Arrays.asList(1, 2).stream().reduce(1, Integer::sum)
                + Arrays.asList(3, 4).stream().reduce(1, Integer::sum);
        System.out.println("invalid identity sequential/partitioned=" + invalidSequential + "/" + invalidPartitions);
    }
}
