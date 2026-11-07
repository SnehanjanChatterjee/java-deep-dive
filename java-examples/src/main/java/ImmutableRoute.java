import java.util.Arrays;
import java.util.Objects;

/** Protects an array-backed value at input and output boundaries. @author Snehanjan Chatterjee */
public final class ImmutableRoute {
    private final int[] stops;
    /** Takes an independent snapshot of the supplied primitive array. */
    public ImmutableRoute(final int[] stops) { this.stops = Objects.requireNonNull(stops, "stops").clone(); }
    /** Returns a copy so the caller cannot change this route. */
    public int[] stops() { return stops.clone(); }
    /** Compares the contents that define the route value. */
    @Override public boolean equals(final Object other) {
        return this == other || other instanceof ImmutableRoute
                && Arrays.equals(stops, ((ImmutableRoute) other).stops);
    }
    /** Hashes the same component values used in equals. */
    @Override public int hashCode() { return Arrays.hashCode(stops); }
    /** Attempts mutation at both boundaries and then compares equal routes. */
    public static void main(final String[] args) {
        final int[] input = {2, 4};
        final ImmutableRoute route = new ImmutableRoute(input);
        input[0] = 9;
        route.stops()[0] = 8;
        System.out.println(route.stops()[0]);
        System.out.println(route.equals(new ImmutableRoute(new int[] {2, 4})));
    }
}
