import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks failed unmatched admission and a successful finite rendezvous. @author Snehanjan Chatterjee */
public final class SynchronousHandoffLessonTest {
    @Test(timeout = 10000) public void testHandoffRequiresReceiverAndStoresNoElement() throws InterruptedException {
        assertArrayEquals(new String[] {"unpaired accepted=false", "received=payload", "stored=0"},
            SynchronousHandoffLesson.observations());
    }
}
