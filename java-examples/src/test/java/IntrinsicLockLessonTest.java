import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Checks receiver, class and private-guard monitor identities. @author Snehanjan Chatterjee */
public final class IntrinsicLockLessonTest {
    @Test public void testDistinctMonitorsAndMatchingRelease() {
        final IntrinsicLockLesson first = new IntrinsicLockLesson();
        final IntrinsicLockLesson second = new IntrinsicLockLesson();
        synchronized (first) {
            assertTrue(first.holdsReceiver());
            assertFalse(Thread.holdsLock(second));
            assertFalse(Thread.holdsLock(IntrinsicLockLesson.class));
        }
        assertFalse(Thread.holdsLock(first));
        assertTrue(first.holdsPrivateGuardOnly());
        assertTrue(IntrinsicLockLesson.holdsClassMonitor());
        assertFalse(Thread.holdsLock(IntrinsicLockLesson.class));
    }
}
