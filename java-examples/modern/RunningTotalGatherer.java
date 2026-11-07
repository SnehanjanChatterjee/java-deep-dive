import java.util.stream.Gatherer;
import java.util.stream.Stream;

/** Builds a sequential intermediate transformation using the stable Java 24 API. @author Snehanjan Chatterjee */
public final class RunningTotalGatherer {
    /** Supplies fresh running-total state and respects downstream cancellation. */
    public static Gatherer<Integer, ?, Long> runningTotals() {
        return Gatherer.<Integer, long[], Long>ofSequential(() -> new long[1], (state, value, downstream) -> {
            state[0] = Math.addExact(state[0], value.longValue());
            return downstream.push(state[0]);
        });
    }
    /** Shows that running totals can continue through a later intermediate operation. */
    public static void main(final String[] args) {
        System.out.println(Stream.of(1, 2, 3, 4).gather(runningTotals()).toList());
        System.out.println(Stream.of(1, 2, 3, 4).gather(runningTotals()).limit(2).toList());
    }
}
