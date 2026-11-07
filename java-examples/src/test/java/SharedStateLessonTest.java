import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks the chosen model and actual finite counter contract. @author Snehanjan Chatterjee */
public final class SharedStateLessonTest {
    @Test public void testChosenScheduleOverwritesOneIncrement() { assertEquals(1, SharedStateLesson.chosenLostUpdate()); }
    @Test public void testBothRealWorkersContribute() throws InterruptedException { assertEquals(20_000, SharedStateLesson.countSafely(10_000)); }
    @Test public void testEmptyWork() throws InterruptedException { assertEquals(0, SharedStateLesson.countSafely(0)); }
    @Test public void testRejectsNegativeAndUnrepresentableTotals() {
        assertThrows(IllegalArgumentException.class, () -> SharedStateLesson.countSafely(-1));
        assertThrows(ArithmeticException.class, () -> SharedStateLesson.countSafely(Integer.MAX_VALUE));
    }
}
