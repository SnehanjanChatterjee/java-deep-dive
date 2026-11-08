import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks each completed contribution is reflected in the final aggregates. @author Snehanjan Chatterjee */
public final class AtomicStatisticsLessonTest {
    @Test public void testQuiescentSumAndMaximum() throws InterruptedException {
        assertArrayEquals(new long[] {12, 3}, AtomicStatisticsLesson.summarize());
    }
}
