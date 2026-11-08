import java.util.List;
import java.util.Objects;
import java.util.stream.Gatherers;

/** Keeps ordered results while bounding one concurrent mapping stage. @author Snehanjan Chatterjee */
public final class ConcurrentGatherer {
    /** Labels non-null integers in encounter order with a positive per-evaluation concurrency bound. */
    public static List<String> labels(final List<Integer> values, final int maxConcurrency) {
        Objects.requireNonNull(values, "values");
        if (maxConcurrency < 1) { throw new IllegalArgumentException("positive concurrency required"); }
        return values.stream().gather(Gatherers.mapConcurrent(maxConcurrency,
                value -> "v" + Objects.requireNonNull(value, "element"))).toList();
    }

    /** Demonstrates the result contract without asserting worker completion order. */
    public static void main(final String[] args) {
        System.out.println("mapConcurrent=" + labels(List.of(1, 2, 3), 2));
    }
}
