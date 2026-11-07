import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks recursive base cases, progress and numeric boundaries. @author Snehanjan Chatterjee */
public final class MethodsRecursionTest {
    @Test public void testFactorialBaseAndRecursiveCases() {
        assertEquals(1L, MethodsRecursion.factorial(0));
        assertEquals(1L, MethodsRecursion.factorial(1));
        assertEquals(24L, MethodsRecursion.factorial(4));
        assertEquals(2_432_902_008_176_640_000L, MethodsRecursion.factorial(20));
    }
    @Test public void testRejectInvalidFactorialAndOverflowingDouble() {
        assertThrows(IllegalArgumentException.class, () -> MethodsRecursion.factorial(-1));
        assertThrows(IllegalArgumentException.class, () -> MethodsRecursion.factorial(21));
        assertThrows(ArithmeticException.class, () -> MethodsRecursion.twice(Integer.MAX_VALUE));
        assertEquals(-6, MethodsRecursion.twice(-3));
    }
    @Test public void testIterativeFactorialContract() {
        assertEquals(1L, MethodsRecursion.factorialIterative(0));
        assertEquals(24L, MethodsRecursion.factorialIterative(4));
        assertEquals(2_432_902_008_176_640_000L, MethodsRecursion.factorialIterative(20));
        assertThrows(IllegalArgumentException.class, () -> MethodsRecursion.factorialIterative(-1));
        assertThrows(IllegalArgumentException.class, () -> MethodsRecursion.factorialIterative(21));
    }
}
