import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Separates element transformations from terminal reductions. @author Snehanjan Chatterjee */
public final class StreamReductionLab {
    /** Demonstrates identity, optional reduction and primitive specialization. */
    public static void main(final String[] args) {
        final List<Integer> values = Arrays.asList(1, 2, 3, 4);
        System.out.println(values.stream().reduce(0, Integer::sum));
        final Optional<Integer> maximum = values.stream().reduce(Integer::max);
        System.out.println(maximum.orElse(-1));
        System.out.println(Collections.<Integer>emptyList().stream().reduce(Integer::max).isPresent());
        System.out.println(values.stream().mapToInt(Integer::intValue).sum());
    }
}
