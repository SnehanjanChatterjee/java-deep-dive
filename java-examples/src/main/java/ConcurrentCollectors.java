import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Compares concurrent collector results without promising concurrent encounter order. @author Snehanjan Chatterjee */
public final class ConcurrentCollectors {
    /** Aggregates values with an associative merge and displays a sorted detached view. */
    public static void main(final String[] args) {
        final List<String> words = Arrays.asList("java", "jvm", "java");
        final ConcurrentMap<String, Long> counts = words.parallelStream().collect(Collectors.toConcurrentMap(
                Function.identity(), word -> 1L, Long::sum));
        System.out.println("concurrent map=" + new TreeMap<>(counts));
        final ConcurrentMap<Integer, Long> lengths = words.parallelStream().collect(Collectors.groupingByConcurrent(
                String::length, Collectors.counting()));
        System.out.println("concurrent groups=" + new TreeMap<>(lengths));
        final Map<Integer, List<String>> groups = words.parallelStream().collect(Collectors.groupingByConcurrent(String::length));
        System.out.println("group cardinalities=" + groups.get(3).size() + "/" + groups.get(4).size());
    }
}
