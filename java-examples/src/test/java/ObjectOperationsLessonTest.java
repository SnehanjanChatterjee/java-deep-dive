import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/** Verifies clone aliasing, constructor copying and class-wide instance identity. */
public final class ObjectOperationsLessonTest {
    @Test public void testShallowAndIndependentCopies() {
        final int[] input = {1};
        final ObjectOperationsLesson.Stamp original = new ObjectOperationsLesson.Stamp("A", input);
        input[0] = 2;
        final ObjectOperationsLesson.Stamp shallow = original.shallowCopy();
        final ObjectOperationsLesson.Stamp independent = original.independentCopy();
        assertNotSame(original, shallow);
        assertNotSame(original, independent);
        shallow.mark(0, 9);
        assertEquals(9, original.markAt(0));
        assertEquals(1, independent.markAt(0));
        assertEquals("A[9]", original.toString());
        assertSame(ObjectOperationsLesson.Stamp.class, original.getClass());
    }
    @Test public void testEmptyNullAndBoundsPolicies() {
        assertEquals("A[]", new ObjectOperationsLesson.Stamp("A", new int[0]).toString());
        assertThrows(NullPointerException.class, () -> new ObjectOperationsLesson.Stamp(null, new int[0]));
        assertThrows(NullPointerException.class, () -> new ObjectOperationsLesson.Stamp("A", null));
        final ObjectOperationsLesson.Stamp stamp = new ObjectOperationsLesson.Stamp("A", new int[0]);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> stamp.mark(0, 2));
    }
    @Test public void testSharedInstanceIsStable() {
        assertSame(ObjectOperationsLesson.instance(), ObjectOperationsLesson.instance());
    }
}
