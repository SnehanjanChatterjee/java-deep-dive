import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Traces intermediate operations as changes of values, types and cardinality. @author Snehanjan Chatterjee */
public final class StreamOperations {
    /** Returns the second and third distinct positive values in descending order when present. */
    public static List<Integer> ranked(final List<Integer> values) {
        Objects.requireNonNull(values, "values");
        return values.stream().filter(Objects::nonNull).filter(value -> value > 0).distinct()
                .sorted(Comparator.reverseOrder()).skip(1).limit(2).collect(Collectors.toList());
    }
    /** Demonstrates each mapping shape and the semantic order of stateful operations. */
    public static void main(final String[] args) {
        System.out.println(ranked(Arrays.asList(3, -1, 3, 1, 4, null, 2)));
        final List<Integer> lengths = Stream.of("a", "long", "bb").map(String::length).collect(Collectors.toList());
        System.out.println("map=" + lengths);
        final List<String> flat = Stream.of("A,B", "", "C")
                .flatMap(line -> line.isEmpty() ? Stream.<String>empty() : Arrays.stream(line.split(",")))
                .collect(Collectors.toList());
        System.out.println("flatMap=" + flat);
        final List<String> nullable = Stream.of("x").map(value -> (String) null).collect(Collectors.toList());
        System.out.println("map null=" + nullable);
        System.out.println("null inner stream count=" + Stream.of("x").flatMap(value -> (Stream<String>) null).count());
        System.out.println("mapToInt sum=" + Stream.of("a", "long", "bb").mapToInt(String::length).sum());
        System.out.println("flatMapToInt sum=" + Stream.of("1,2", "3")
                .flatMapToInt(line -> Arrays.stream(line.split(",")).mapToInt(Integer::parseInt)).sum());
        System.out.println("flatMapToLong sum=" + Stream.of("10000000000,20000000000")
                .flatMapToLong(line -> Arrays.stream(line.split(",")).mapToLong(Long::parseLong)).sum());
        System.out.println("flatMapToDouble sum=" + Stream.of("1.5,2.0", "3.0")
                .flatMapToDouble(line -> Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble)).sum());
        final List<Integer> limitFirst = Stream.of(3, 3, 2).limit(2).distinct().collect(Collectors.toList());
        final List<Integer> distinctFirst = Stream.of(3, 3, 2).distinct().limit(2).collect(Collectors.toList());
        System.out.println("limit/distinct=" + limitFirst + "/" + distinctFirst);
        final List<String> stable = Stream.of("bb", "aa", "c").sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
        System.out.println("stable ties=" + stable);
        final List<String> changed = new ArrayList<>(flat);
        changed.add("D");
        System.out.println("independent source=" + flat);
    }
}
