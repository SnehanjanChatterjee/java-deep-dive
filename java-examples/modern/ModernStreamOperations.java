import java.util.List;
import java.util.stream.Stream;

/** Distinguishes Java 9 prefix operations from Java 16 multi-emission and list collection. @author Snehanjan Chatterjee */
public final class ModernStreamOperations {
    /** Runs finite examples rather than consuming unbounded sources indefinitely. */
    public static void main(final String[] args) {
        System.out.println("ofNullable=" + Stream.ofNullable(null).count());
        System.out.println("finite iterate=" + Stream.iterate(1, value -> value <= 5, value -> value + 1).toList());
        System.out.println("takeWhile=" + Stream.of(1, 2, 0, 3).takeWhile(value -> value > 0).toList());
        System.out.println("filter=" + Stream.of(1, 2, 0, 3).filter(value -> value > 0).toList());
        System.out.println("dropWhile=" + Stream.of(1, 2, 0, 3).dropWhile(value -> value > 0).toList());
        final List<Integer> emitted = Stream.of(1, 2, 3).<Integer>mapMulti((value, downstream) -> {
            if (value % 2 == 0) { downstream.accept(value); downstream.accept(-value); }
        }).toList();
        System.out.println("mapMulti=" + emitted);
        try { emitted.add(9); throw new IllegalStateException("expected unmodifiable result"); }
        catch (final UnsupportedOperationException expected) { System.out.println("toList mutation rejected"); }
        System.out.println("toList retains null=" + Stream.of("A", null).toList().get(1));
    }
}
