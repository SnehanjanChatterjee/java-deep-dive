import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks two deliberately different null policies. */
public final class WrapperBoundariesLessonTest {
    @Test public void testOptionalValuePolicy() {
        assertEquals(0, WrapperBoundariesLesson.orZero(null));
        assertEquals(0, WrapperBoundariesLesson.orZero(0));
        assertEquals(-3, WrapperBoundariesLesson.orZero(-3));
        assertEquals(Integer.MAX_VALUE, WrapperBoundariesLesson.orZero(Integer.MAX_VALUE));
    }
    @Test public void testRequiredValuePolicy() {
        assertEquals(0, WrapperBoundariesLesson.requireInt(0));
        assertEquals(Integer.MIN_VALUE, WrapperBoundariesLesson.requireInt(Integer.MIN_VALUE));
        assertThrows(NullPointerException.class, () -> WrapperBoundariesLesson.requireInt(null));
    }
}
