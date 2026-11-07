import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks shared versus separate object identity and completed worker updates. */
public final class InvocationLocalCounterLessonTest {
    @Test public void testBothInvocationsCanChangeTheSameObject() {
        final InvocationLocalCounterLesson.Counter shared = new InvocationLocalCounterLesson.Counter();
        InvocationLocalCounterLesson.addTwice(shared);
        InvocationLocalCounterLesson.addTwice(shared);
        assertEquals(4, shared.value());
    }

    @Test public void testSeparateObjectsKeepSeparateFields() {
        final InvocationLocalCounterLesson.Counter first = new InvocationLocalCounterLesson.Counter();
        final InvocationLocalCounterLesson.Counter second = new InvocationLocalCounterLesson.Counter();
        InvocationLocalCounterLesson.addTwice(first);
        assertEquals(2, first.value());
        assertEquals(0, second.value());
        InvocationLocalCounterLesson.addTwice(second);
        assertEquals(2, first.value());
        assertEquals(2, second.value());
    }

    @Test public void testBothWorkersCompleteTheirTwoUpdates() throws InterruptedException {
        final InvocationLocalCounterLesson.Counter shared = new InvocationLocalCounterLesson.Counter();
        final Thread first = new Thread(() -> InvocationLocalCounterLesson.addTwice(shared));
        final Thread second = new Thread(() -> InvocationLocalCounterLesson.addTwice(shared));
        first.start(); second.start();
        first.join(); second.join();
        assertEquals(4, shared.value());
    }

    @Test public void testNullCounterIsRejected() {
        assertThrows(NullPointerException.class, () -> InvocationLocalCounterLesson.addTwice(null));
    }
}
