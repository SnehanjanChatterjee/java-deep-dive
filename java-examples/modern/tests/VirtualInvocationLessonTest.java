import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks the virtual Thread's documented direct-run behavior separately from start. @author Snehanjan Chatterjee */
public final class VirtualInvocationLessonTest {
    @Test public void testDirectVirtualRunDoesNotInvokeTask() throws InterruptedException {
        assertArrayEquals(new String[] {"after virtual run: calls=0, state=NEW",
            "after start/join: calls=1, virtual=true"}, VirtualInvocationLesson.observations());
    }
}
