import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/** A domain collector with independent mutable partitions and an immutable result. @author Snehanjan Chatterjee */
public final class CorePurchaseCollector {
    public static final class Purchase {
        private final String customer;
        private final long cents;
        /** Validates one fixed-currency purchase amount. */
        public Purchase(final String customer, final long cents) {
            this.customer = Objects.requireNonNull(customer, "customer");
            if (cents < 0) { throw new IllegalArgumentException("negative purchase"); }
            this.cents = cents;
        }
    }
    public static final class Stats {
        private final long count;
        private final long total;
        /** Creates an immutable aggregate after reduction. */
        private Stats(final long count, final long total) { this.count = count; this.total = total; }
        /** Returns aggregate count. */
        public long count() { return count; }
        /** Returns total cents. */
        public long total() { return total; }
        /** Renders the demonstration without requiring map encounter accidents. */
        @Override public String toString() { return count + ":" + total; }
    }
    private static final class Accumulator {
        private long count;
        private long total;
        /** Adds one item without partially assigning fields if arithmetic fails. */
        private void add(final Purchase purchase) {
            Objects.requireNonNull(purchase, "purchase");
            final long nextCount = Math.addExact(count, 1);
            final long nextTotal = Math.addExact(total, purchase.cents);
            count = nextCount;
            total = nextTotal;
        }
        /** Combines independently owned partial state. */
        private Accumulator merge(final Accumulator other) {
            final long nextCount = Math.addExact(count, other.count);
            final long nextTotal = Math.addExact(total, other.total);
            count = nextCount;
            total = nextTotal;
            return this;
        }
        /** Detaches an immutable result from the mutable accumulation phase. */
        private Stats finish() { return new Stats(count, total); }
    }
    /** Supplies no CONCURRENT or IDENTITY_FINISH promise. */
    public static Collector<Purchase, ?, Stats> totals() {
        return Collector.of(Accumulator::new, Accumulator::add, Accumulator::merge, Accumulator::finish);
    }
    /** Groups by customer with a deterministic map type and a downstream collector. */
    public static Map<String, Stats> byCustomer(final List<Purchase> purchases, final boolean parallel) {
        Objects.requireNonNull(purchases, "purchases");
        return (parallel ? purchases.parallelStream() : purchases.stream()).collect(
                Collectors.groupingBy(purchase -> purchase.customer, TreeMap::new, totals()));
    }
    /** Demonstrates matching sequential and parallel aggregates. */
    public static void main(final String[] args) {
        final List<Purchase> purchases = Arrays.asList(new Purchase("A", 100), new Purchase("B", 300), new Purchase("A", 200));
        System.out.println(byCustomer(purchases, false));
        System.out.println(byCustomer(purchases, true));
    }
}
