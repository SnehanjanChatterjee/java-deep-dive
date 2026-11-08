import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Verifies both publication directions without volatile fields. @author Snehanjan Chatterjee */
public final class MemoryRoundTripLessonTest {
    @Test public void testInputStartResultJoinRoundTrip() throws InterruptedException {
        assertEquals(42, MemoryRoundTripLesson.calculate(21));
        assertEquals(0, MemoryRoundTripLesson.calculate(0));
        assertEquals(-42, MemoryRoundTripLesson.calculate(-21));
    }
}
