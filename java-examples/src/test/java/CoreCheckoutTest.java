import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Protects quote invariants without mocking arithmetic. @author Snehanjan Chatterjee */
public final class CoreCheckoutTest {
    @Test public void testSuccessEmptyAndFullDiscount() {
        assertEquals(2500L, CoreCheckout.quote(Arrays.asList(new CoreCheckout.Line(1250, 2),
                new CoreCheckout.Line(500, 1)), subtotal -> 500).totalCents());
        assertEquals(0L, CoreCheckout.quote(Collections.<CoreCheckout.Line>emptyList(), subtotal -> 0).totalCents());
        assertEquals(0L, CoreCheckout.quote(Collections.singletonList(new CoreCheckout.Line(7, 2)), subtotal -> subtotal).totalCents());
    }
    @Test public void testNullAndRejectedInputs() {
        assertThrows(NullPointerException.class, () -> CoreCheckout.quote(null, subtotal -> 0));
        assertThrows(NullPointerException.class, () -> CoreCheckout.quote(Collections.singletonList(null), subtotal -> 0));
        assertThrows(NullPointerException.class, () -> CoreCheckout.quote(Collections.<CoreCheckout.Line>emptyList(), null));
        assertThrows(IllegalArgumentException.class, () -> new CoreCheckout.Line(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> new CoreCheckout.Line(1, 0));
        assertThrows(IllegalArgumentException.class, () -> CoreCheckout.quote(Collections.<CoreCheckout.Line>emptyList(), subtotal -> -1));
        assertThrows(IllegalArgumentException.class, () -> CoreCheckout.quote(Collections.<CoreCheckout.Line>emptyList(), subtotal -> 1));
    }
    @Test public void testOverflowIsReported() {
        assertThrows(ArithmeticException.class, () -> CoreCheckout.quote(
                Collections.singletonList(new CoreCheckout.Line(Long.MAX_VALUE, 2)), subtotal -> 0));
        assertThrows(ArithmeticException.class, () -> CoreCheckout.quote(
                Arrays.asList(new CoreCheckout.Line(Long.MAX_VALUE, 1), new CoreCheckout.Line(1, 1)), subtotal -> 0));
    }
}
