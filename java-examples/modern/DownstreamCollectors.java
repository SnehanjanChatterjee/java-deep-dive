import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/** Separates Java 9 downstream adapters, Java 10 immutable results and Java 12 teeing. @author Snehanjan Chatterjee */
public final class DownstreamCollectors {
    public static final class Summary {
        private final long count;
        private final int total;
        /** Publishes only completed aggregate values. */
        private Summary(final long count, final int total) { this.count = count; this.total = total; }
        /** Keeps empty input distinct from a real average of zero. */
        public OptionalDouble average() { return count == 0 ? OptionalDouble.empty() : OptionalDouble.of((double) total / count); }
    }
    /** Retains group keys while transforming each group's downstream reduction. */
    public static void main(final String[] args) {
        final List<String> words = Arrays.asList("ants", "ape", "bee", "bob");
        final Map<Character, List<String>> downstream = words.stream().collect(Collectors.groupingBy(
                word -> word.charAt(0), TreeMap::new, Collectors.filtering(word -> word.length() > 3, Collectors.toList())));
        final Map<Character, List<String>> upstream = words.stream().filter(word -> word.length() > 3)
                .collect(Collectors.groupingBy(word -> word.charAt(0), TreeMap::new, Collectors.toList()));
        System.out.println("downstream filter=" + downstream);
        System.out.println("upstream filter=" + upstream);
        final Map<Character, Set<String>> letters = words.stream().collect(Collectors.groupingBy(
                word -> word.charAt(0), TreeMap::new,
                Collectors.flatMapping(word -> Arrays.stream(word.split("")), Collectors.toCollection(TreeSet::new))));
        System.out.println("downstream flatMap=" + letters);
        final Summary summary = words.stream().collect(Collectors.teeing(
                Collectors.counting(), Collectors.summingInt(String::length), Summary::new));
        System.out.println("tee average=" + summary.average().orElse(-1));
        final Summary empty = List.<String>of().stream().collect(Collectors.teeing(
                Collectors.counting(), Collectors.summingInt(String::length), Summary::new));
        System.out.println("empty average present=" + empty.average().isPresent());
        final List<String> readOnly = words.stream().collect(Collectors.toUnmodifiableList());
        try { readOnly.add("invalid"); throw new IllegalStateException("expected unmodifiable result"); }
        catch (final UnsupportedOperationException expected) { System.out.println("unmodifiable collector mutation rejected"); }
    }
}
