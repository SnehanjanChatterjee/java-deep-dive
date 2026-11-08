import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Checks invalid domain, square boundaries and the maximum int prime. */
public final class PrimeTrialDivisionLessonTest {
    @Test public void testNonPrimeAndBoundaryInputs() {
        for (final int value : new int[] {Integer.MIN_VALUE, -1, 0, 1, 4, 9, 49, 100}) {
            assertFalse(PrimeTrialDivisionLesson.isPrime(value));
        }
    }
    @Test public void testPrimesIncludingOverflowSensitiveBoundary() {
        for (final int value : new int[] {2, 3, 5, 97, Integer.MAX_VALUE}) {
            assertTrue(PrimeTrialDivisionLesson.isPrime(value));
        }
    }
}
