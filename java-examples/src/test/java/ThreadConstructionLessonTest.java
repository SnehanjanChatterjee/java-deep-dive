import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Verifies all construction forms complete their own independent result slot. @author Snehanjan Chatterjee */
public final class ThreadConstructionLessonTest {
    @Test public void testAllFormsPublishAfterJoin() throws InterruptedException {
        assertArrayEquals(new String[] {"Thread subclass", "named Runnable", "anonymous Runnable", "lambda Runnable"},
            ThreadConstructionLesson.runForms());
    }
}
