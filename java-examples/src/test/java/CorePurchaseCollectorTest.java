import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.stream.Collector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

/** Tests partition combination, result shape and failure contracts. @author Snehanjan Chatterjee */
public final class CorePurchaseCollectorTest {
    @Test public void testGroupedSequentialAndParallelTotals() {
        final List<CorePurchaseCollector.Purchase> values = Arrays.asList(new CorePurchaseCollector.Purchase("A", 100),
                new CorePurchaseCollector.Purchase("B", 300), new CorePurchaseCollector.Purchase("A", 200));
        for (final boolean parallel : new boolean[] {false, true}) {
            final Map<String, CorePurchaseCollector.Stats> result = CorePurchaseCollector.byCustomer(values, parallel);
            assertEquals(2L, result.get("A").count()); assertEquals(300L, result.get("A").total());
            assertEquals(1L, result.get("B").count()); assertEquals(300L, result.get("B").total());
            assertEquals(200L, result.get("A").maximum().getAsLong());
            assertEquals(300L, result.get("B").maximum().getAsLong());
        }
        final CorePurchaseCollector.Stats empty = Collections.<CorePurchaseCollector.Purchase>emptyList().stream().collect(CorePurchaseCollector.totals());
        assertEquals(0L, empty.count()); assertEquals(0L, empty.total());
        assertFalse(empty.maximum().isPresent());
    }
    @Test public void testMaximumEmptyIdentityAndExplicitPartitions() {
        final List<CorePurchaseCollector.Purchase> values = Arrays.asList(new CorePurchaseCollector.Purchase("A", 100),
                new CorePurchaseCollector.Purchase("A", 200));
        final List<CorePurchaseCollector.Purchase> empty = Collections.emptyList();
        assertEquals(200L, combine(CorePurchaseCollector.totals(), values, empty).maximum().getAsLong());
        assertEquals(200L, combine(CorePurchaseCollector.totals(), empty, values).maximum().getAsLong());
        assertFalse(combine(CorePurchaseCollector.totals(), empty, empty).maximum().isPresent());
        assertEquals(200L, combine(CorePurchaseCollector.totals(), values.subList(0, 1), values.subList(1, 2)).maximum().getAsLong());
        assertEquals(0L, Collections.singletonList(new CorePurchaseCollector.Purchase("A", 0)).stream()
                .collect(CorePurchaseCollector.totals()).maximum().getAsLong());
        verifyDetachedFinish(CorePurchaseCollector.totals());
    }
    @Test public void testNullNegativeAndOverflow() {
        assertThrows(NullPointerException.class, () -> new CorePurchaseCollector.Purchase(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new CorePurchaseCollector.Purchase("A", -1));
        assertThrows(NullPointerException.class, () -> CorePurchaseCollector.byCustomer(null, false));
        assertThrows(NullPointerException.class, () -> Arrays.asList((CorePurchaseCollector.Purchase) null).stream().collect(CorePurchaseCollector.totals()));
        assertThrows(ArithmeticException.class, () -> Arrays.asList(new CorePurchaseCollector.Purchase("A", Long.MAX_VALUE),
                new CorePurchaseCollector.Purchase("A", 1)).stream().collect(CorePurchaseCollector.totals()));
    }
    /** Captures the private accumulator type through the public collector contract. */
    private static <A> CorePurchaseCollector.Stats combine(final Collector<CorePurchaseCollector.Purchase, A, CorePurchaseCollector.Stats> collector,
            final List<CorePurchaseCollector.Purchase> left, final List<CorePurchaseCollector.Purchase> right) {
        final A first = collector.supplier().get(); final A second = collector.supplier().get();
        assertNotSame(first, second);
        left.forEach(value -> collector.accumulator().accept(first, value));
        right.forEach(value -> collector.accumulator().accept(second, value));
        return collector.finisher().apply(collector.combiner().apply(first, second));
    }
    /** Checks the finisher does not retain an alias to mutable accumulation state. */
    private static <A> void verifyDetachedFinish(final Collector<CorePurchaseCollector.Purchase, A, CorePurchaseCollector.Stats> collector) {
        final A state = collector.supplier().get();
        collector.accumulator().accept(state, new CorePurchaseCollector.Purchase("A", 100));
        final CorePurchaseCollector.Stats snapshot = collector.finisher().apply(state);
        collector.accumulator().accept(state, new CorePurchaseCollector.Purchase("A", 300));
        assertEquals(1L, snapshot.count()); assertEquals(100L, snapshot.total());
        assertEquals(100L, snapshot.maximum().getAsLong());
    }
}
