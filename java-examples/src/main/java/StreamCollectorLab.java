import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/** Demonstrates partition-owned mutable reduction. @author Snehanjan Chatterjee */
public final class StreamCollectorLab {
    private static final class Summary {
        private long count;
        private long total;
        /** Incorporates one input value. */
        private void accept(final Integer value) { count++; total += value; }
        /** Merges an independently accumulated partition. */
        private Summary combine(final Summary other) { count += other.count; total += other.total; return this; }
        /** Renders the result for the demonstration. */
        @Override public String toString() { return "count=" + count + ", total=" + total; }
    }
    /** Creates a nonconcurrent collector with independently supplied partial containers. */
    private static Collector<Integer, Summary, Summary> summary() {
        return Collector.of(Summary::new, Summary::accept, Summary::combine);
    }
    /** Shows parallel combination and downstream counting. */
    public static void main(final String[] args) {
        final List<Integer> values = Arrays.asList(1, 2, 3, 4);
        System.out.println(values.parallelStream().collect(summary()));
        final Map<String, Long> counts = values.stream().collect(Collectors.groupingBy(
                value -> value % 2 == 0 ? "even" : "odd", TreeMap::new, Collectors.counting()));
        System.out.println(counts);
    }
}
