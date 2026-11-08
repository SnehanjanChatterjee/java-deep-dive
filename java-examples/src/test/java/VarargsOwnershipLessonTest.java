import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

/** Checks empty, array, null, overflow and alias boundaries. */
public final class VarargsOwnershipLessonTest {
    @Test public void testSumsAndFailurePolicy() {
        assertEquals(0, VarargsOwnershipLesson.sum());
        assertEquals(3, VarargsOwnershipLesson.sum(3));
        assertEquals(0, VarargsOwnershipLesson.sum(-4, 4));
        assertEquals(9, VarargsOwnershipLesson.sum(new int[] {2, 3, 4}));
        assertThrows(NullPointerException.class, () -> VarargsOwnershipLesson.sum((int[]) null));
        assertThrows(ArithmeticException.class, () -> VarargsOwnershipLesson.sum(Integer.MAX_VALUE, 1));
    }
    @Test public void testSnapshotDetachesCallerArray() {
        final int[] source = {5, 6};
        final int[] copy = VarargsOwnershipLesson.snapshot(source);
        assertNotSame(source, copy);
        assertArrayEquals(source, copy);
        copy[0] = 99;
        assertEquals(5, source[0]);
        assertEquals(0, VarargsOwnershipLesson.snapshot().length);
        assertThrows(NullPointerException.class, () -> VarargsOwnershipLesson.snapshot((int[]) null));
    }
}
