import java.util.stream.Gatherers;
import java.util.stream.Stream;

/** Gives each stable gatherer family a small, finite observable result. @author Snehanjan Chatterjee */
public final class BuiltInGatherers {
    /** Compares prefix/final reduction, window boundaries and ordered concurrent mapping. */
    public static void main(final String[] args) {
        System.out.println("scan=" + Stream.of(1, 2, 3).gather(Gatherers.scan(() -> 0, Integer::sum)).toList());
        System.out.println("fold=" + Stream.of(1, 2, 3).gather(Gatherers.fold(() -> 0, Integer::sum)).toList());
        System.out.println("fixed=" + Stream.of(1, 2, 3).gather(Gatherers.windowFixed(2)).toList());
        System.out.println("sliding=" + Stream.of(1, 2, 3).gather(Gatherers.windowSliding(2)).toList());
        System.out.println("short sliding=" + Stream.of(1).gather(Gatherers.windowSliding(3)).toList());
        System.out.println("empty windows=" + Stream.empty().gather(Gatherers.windowFixed(3)).toList());
        System.out.println("mapConcurrent=" + Stream.of(1, 2, 3).gather(Gatherers.mapConcurrent(2, value -> "v" + value)).toList());
    }
}
