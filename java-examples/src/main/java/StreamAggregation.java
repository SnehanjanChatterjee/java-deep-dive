import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Reason about lazy pipelines, reductions and safe aggregation. @author Snehanjan Chatterjee */
public final class StreamAggregation {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final List<String> lines = Arrays.asList("Java JVM", "java");
        final Map<String, Long> counts = lines.stream()
            .flatMap(line -> Arrays.stream(line.toLowerCase(Locale.ROOT).split(" ")))
            .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
        System.out.println(counts);
    }
}
