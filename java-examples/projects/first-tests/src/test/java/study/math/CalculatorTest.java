package study.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Makes expected values, boundaries and test discovery visible. @author Snehanjan Chatterjee */
public final class CalculatorTest {
    @Test public void testPositiveOperands() { assertEquals(5, Calculator.add(2, 3)); }
    @Test public void testZeroAndNegativeOperand() { assertEquals(-4, Calculator.add(0, -4)); }
    @Test public void testIntBoundary() { assertEquals(Integer.MIN_VALUE, Calculator.add(Integer.MAX_VALUE, 1)); }
}
