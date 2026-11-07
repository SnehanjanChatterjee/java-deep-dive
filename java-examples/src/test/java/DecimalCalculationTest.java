import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks the invoice scale and rounding boundary independently of display. @author Snehanjan Chatterjee */
public final class DecimalCalculationTest {
    @Test public void testInvoiceAndZero() {
        assertEquals(new BigDecimal("64.79"), DecimalCalculation.total(new BigDecimal("19.95"), 3, new BigDecimal("0.0825")));
        assertEquals(new BigDecimal("0.00"), DecimalCalculation.total(BigDecimal.ZERO, 1, BigDecimal.ZERO));
        assertEquals(new BigDecimal("1.00"), DecimalCalculation.total(new BigDecimal("1.00"), 1, new BigDecimal("0.005")));
    }
    @Test public void testRejectedInputs() {
        assertThrows(NullPointerException.class, () -> DecimalCalculation.total(null, 1, BigDecimal.ZERO));
        assertThrows(NullPointerException.class, () -> DecimalCalculation.total(BigDecimal.ONE, 1, null));
        assertThrows(IllegalArgumentException.class, () -> DecimalCalculation.total(new BigDecimal("-1"), 1, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> DecimalCalculation.total(BigDecimal.ONE, 0, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> DecimalCalculation.total(BigDecimal.ONE, 1, new BigDecimal("-1")));
        assertThrows(ArithmeticException.class, () -> DecimalCalculation.total(new BigDecimal("1.001"), 1, BigDecimal.ZERO));
    }
}
