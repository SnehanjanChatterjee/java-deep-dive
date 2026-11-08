import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Checks protected observations and released ownership after downgrading. @author Snehanjan Chatterjee */
public final class ReadWriteDowngradeLessonTest {
    @Test public void testRepeatedDowngradeRetainsValueAndReleasesHolds() {
        final ReadWriteDowngradeLesson state = new ReadWriteDowngradeLesson();
        assertEquals(42, state.updateAndRead(42));
        assertTrue(state.callerHasNoHolds());
        assertEquals(-1, state.updateAndRead(-1));
        assertTrue(state.callerHasNoHolds());
    }
}
