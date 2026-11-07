import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Demonstrates ordinary collectors before building a custom reduction. @author Snehanjan Chatterjee */
public final class CollectorRecipes {
    /** Groups, counts, joins and deliberately merges repeated words. */
    public static void main(final String[] args) {
        final List<String> words = Arrays.asList("java", "jvm", "java");
        final Map<Integer, List<String>> byLength = words.stream().collect(
                Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println(byLength);
        System.out.println(words.stream().collect(Collectors.joining(", ")));
        final Map<String, Integer> counts = words.stream().collect(
                Collectors.toMap(Function.identity(), word -> 1, Integer::sum, TreeMap::new));
        System.out.println(counts);
        final Map<Integer, Long> countsByLength = words.stream().collect(Collectors.groupingBy(
                String::length, TreeMap::new, Collectors.counting()));
        System.out.println(countsByLength);
    }
}
