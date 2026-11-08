import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** Creates sources with declared bounds and deliberately chosen element types. @author Snehanjan Chatterjee */
public final class StreamSources {
    /** Compares object/primitive arrays, ranges, builders, generators and map views. */
    public static void main(final String[] args) {
        final String[] words = {"A", "B", "C"};
        System.out.println("array slice=" + Arrays.stream(words, 1, 3).collect(Collectors.toList()));
        final int[] numbers = {1, 2, 3};
        System.out.println("primitive sum=" + Arrays.stream(numbers).sum());
        System.out.println("one array element=" + Stream.of(numbers).count());
        System.out.println("range=" + Arrays.toString(IntStream.range(1, 4).toArray()));
        System.out.println("rangeClosed=" + Arrays.toString(IntStream.rangeClosed(1, 4).toArray()));
        final List<Integer> iteration = Stream.iterate(1, value -> value * 2).limit(4).collect(Collectors.toList());
        System.out.println("iterate=" + iteration);
        final List<String> generation = Stream.generate(() -> "x").limit(3).collect(Collectors.toList());
        System.out.println("generate=" + generation);
        final Stream.Builder<String> builder = Stream.builder();
        builder.add("left").add("right");
        System.out.println("builder=" + builder.build().collect(Collectors.toList()));
        System.out.println("concat=" + Stream.concat(Stream.of("A"), Stream.of("B", "C")).collect(Collectors.toList()));
        final Map<String, Integer> map = new TreeMap<>(); map.put("A", 2); map.put("B", 3);
        System.out.println("map values sum=" + map.values().stream().mapToInt(Integer::intValue).sum());
        System.out.println("entries=" + map.entrySet().stream().map(entry -> entry.getKey() + entry.getValue())
                .collect(Collectors.toList()));
        try (final Stream<Integer> configured = Stream.of(1, 2)) {
            final boolean parallel = configured.parallel().isParallel();
            final Stream<Integer> last = configured.sequential().unordered();
            System.out.println("parallel then sequential=" + parallel + "/" + last.isParallel());
            System.out.println("configured count=" + last.count());
        }
    }
}
