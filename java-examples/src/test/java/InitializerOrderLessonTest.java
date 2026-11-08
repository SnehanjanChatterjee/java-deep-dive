import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks per-object initialization despite constructor delegation. */
public final class InitializerOrderLessonTest {
    @Test public void testDelegatingAndDirectConstructionInitializeOnce() {
        final InitializerOrderLesson.Ticket first = new InitializerOrderLesson.Ticket();
        final InitializerOrderLesson.Ticket second = new InitializerOrderLesson.Ticket(0);
        assertEquals(7, first.value());
        assertEquals(0, second.value());
        assertEquals(1, first.initializerRuns());
        assertEquals(1, second.initializerRuns());
    }
}
