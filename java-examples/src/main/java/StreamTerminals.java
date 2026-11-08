import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/** Distinguishes terminal results, traversal escape hatches and explicit closure. @author Snehanjan Chatterjee */
public final class StreamTerminals {
    /** Uses fresh streams for independent questions instead of reusing a consumed pipeline. */
    public static void main(final String[] args) {
        System.out.println("count=" + Stream.of(3, 1, 2).count());
        System.out.println("min/max=" + Stream.of(3, 1, 2).min(Comparator.naturalOrder()).orElse(-1)
                + "/" + Stream.of(3, 1, 2).max(Comparator.naturalOrder()).orElse(-1));
        System.out.println("first=" + Stream.of(3, 1, 2).findFirst().orElse(-1));
        System.out.println("any present=" + Stream.of(3, 1, 2).findAny().isPresent());
        System.out.println("empty any/all/none=" + Stream.<Integer>empty().anyMatch(value -> value > 0)
                + "/" + Stream.<Integer>empty().allMatch(value -> value > 0)
                + "/" + Stream.<Integer>empty().noneMatch(value -> value > 0));
        final String[] names = Stream.of("A", "B").toArray(String[]::new);
        System.out.println("typed array=" + Arrays.toString(names));
        final List<Integer> actions = new ArrayList<>();
        Stream.of(3, 1, 2).forEachOrdered(actions::add);
        System.out.println("ordered actions=" + actions);
        final Iterator<Integer> cursor = Stream.of(1, 2).iterator();
        int total = 0;
        while (cursor.hasNext()) { total += cursor.next(); }
        System.out.println("iterator total=" + total);
        final List<Integer> traversal = new ArrayList<>();
        Stream.of(4, 5).spliterator().forEachRemaining(traversal::add);
        System.out.println("spliterator=" + traversal);
        final boolean[] closed = {false};
        try (final Stream<String> values = Stream.of("A", "B").onClose(() -> closed[0] = true)) {
            System.out.println("terminal=" + values.count() + ", closed=" + closed[0]);
        }
        System.out.println("after close=" + closed[0]);
        final Optional<Integer> emptyMaximum = Stream.<Integer>empty().max(Comparator.naturalOrder());
        System.out.println("empty maximum=" + emptyMaximum.isPresent());
    }
}
