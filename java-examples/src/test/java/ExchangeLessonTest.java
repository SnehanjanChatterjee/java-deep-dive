import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks both directions of the paired handoff. @author Snehanjan Chatterjee */
public final class ExchangeLessonTest {
    @Test(timeout = 10000) public void testEachParticipantReceivesPeerValue() throws Exception {
        assertArrayEquals(new String[] {"worker-buffer", "owner-buffer"}, ExchangeLesson.exchangePair());
    }
}
