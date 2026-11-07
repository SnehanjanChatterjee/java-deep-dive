import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Verifies a coordinated consumer handoff through completed termination. @author Snehanjan Chatterjee */
public final class MonitorHandoffLessonTest {
    @Test(timeout = 5000) public void testWaitingConsumerReceivesMessage() throws InterruptedException {
        assertEquals("message", MonitorHandoffLesson.exchange());
    }
}
