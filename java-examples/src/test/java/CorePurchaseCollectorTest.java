import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Tests partition combination, result shape and failure contracts. @author Snehanjan Chatterjee */
public final class CorePurchaseCollectorTest {
    @Test public void testGroupedSequentialAndParallelTotals() {
        final List<CorePurchaseCollector.Purchase> values = Arrays.asList(new CorePurchaseCollector.Purchase("A", 100),
                new CorePurchaseCollector.Purchase("B", 300), new CorePurchaseCollector.Purchase("A", 200));
        for (final boolean parallel : new boolean[] {false, true}) {
            final Map<String, CorePurchaseCollector.Stats> result = CorePurchaseCollector.byCustomer(values, parallel);
            assertEquals(2L, result.get("A").count()); assertEquals(300L, result.get("A").total());
            assertEquals(1L, result.get("B").count()); assertEquals(300L, result.get("B").total());
        }
        final CorePurchaseCollector.Stats empty = Collections.<CorePurchaseCollector.Purchase>emptyList().stream().collect(CorePurchaseCollector.totals());
        assertEquals(0L, empty.count()); assertEquals(0L, empty.total());
    }
    @Test public void testNullNegativeAndOverflow() {
        assertThrows(NullPointerException.class, () -> new CorePurchaseCollector.Purchase(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new CorePurchaseCollector.Purchase("A", -1));
        assertThrows(NullPointerException.class, () -> CorePurchaseCollector.byCustomer(null, false));
        assertThrows(NullPointerException.class, () -> Arrays.asList((CorePurchaseCollector.Purchase) null).stream().collect(CorePurchaseCollector.totals()));
        assertThrows(ArithmeticException.class, () -> Arrays.asList(new CorePurchaseCollector.Purchase("A", Long.MAX_VALUE),
                new CorePurchaseCollector.Purchase("A", 1)).stream().collect(CorePurchaseCollector.totals()));
    }
}
