import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.math.BigDecimal;

/** Demonstrates TreeSet contracts. @author Snehanjan Chatterjee */
public final class TreeSetExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final Set<BigDecimal> hashed = new HashSet<>();
        final TreeSet<BigDecimal> sorted = new TreeSet<>();
        for (final BigDecimal value : Arrays.asList(new BigDecimal("1.0"), new BigDecimal("1.00"))) {
            hashed.add(value); sorted.add(value);
        }
        System.out.println(hashed.size());
        System.out.println(sorted.size());
        final TreeSet<Integer> numbers = new TreeSet<>(Arrays.asList(30, 10, 20));
        System.out.println(numbers.lower(20));
        System.out.println(numbers.ceiling(20));
        System.out.println(numbers.descendingSet());
    }
}
