import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/** Checks copied values, shared identity and the returned object's independent state. */
public final class JvmStackHeapLessonTest {
    @Test public void testPrimitiveCopyAndSharedObjectMutation() {
        final int number = 7;
        final JvmStackHeapLesson.Box original = new JvmStackHeapLesson.Box(number);
        final JvmStackHeapLesson.Box alias = original;
        JvmStackHeapLesson.change(original, number);
        assertEquals(7, number);
        assertEquals(8, alias.value());
        assertSame(original, alias);
    }

    @Test public void testFactoryReturnsIndependentObjects() {
        final JvmStackHeapLesson.Box first = JvmStackHeapLesson.createBox(4);
        final JvmStackHeapLesson.Box second = JvmStackHeapLesson.createBox(4);
        assertNotSame(first, second);
        JvmStackHeapLesson.change(first, 10);
        assertEquals(11, first.value());
        assertEquals(4, second.value());
    }

    @Test public void testNullAndOrdinaryIntegerBoundary() {
        assertThrows(NullPointerException.class, () -> JvmStackHeapLesson.change(null, 7));
        final JvmStackHeapLesson.Box box = new JvmStackHeapLesson.Box(0);
        JvmStackHeapLesson.change(box, Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, box.value());
    }
}
