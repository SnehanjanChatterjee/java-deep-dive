import java.util.stream.Gatherers;
import java.util.stream.Stream;

/** Compares fixed and overlapping windows with the stable Java 24+ API. @author Snehanjan Chatterjee */
public final class GathererWindows {
    /** Processes the same finite input with two intermediate window transformations. */
    public static void main(final String[] args) {
        System.out.println(Stream.of(1, 2, 3, 4, 5).gather(Gatherers.windowFixed(3)).toList());
        System.out.println(Stream.of(1, 2, 3, 4, 5).gather(Gatherers.windowSliding(3)).toList());
    }
}
