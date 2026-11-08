import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

/** Checks consuming versus non-consuming inspection on the owned worker. @author Snehanjan Chatterjee */
public final class InterruptStatusLessonTest {
    @Test public void testFlagConsumptionAndSleepException() throws InterruptedException {
        assertArrayEquals(new String[] {"first inspect=true", "second inspect=true", "consume=true",
            "after consume=false", "sleep threw", "after sleep exception=false"}, InterruptStatusLesson.observations());
        assertFalse(Thread.currentThread().isInterrupted());
    }
}
