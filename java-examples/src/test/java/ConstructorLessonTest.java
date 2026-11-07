import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks construction, validation and mutation preserve the score invariant. @author Snehanjan Chatterjee */
public final class ConstructorLessonTest {
    @Test public void testChainingAndIndependentState() {
        final int before = ConstructorLesson.created();
        final ConstructorLesson first = new ConstructorLesson("Ada");
        final ConstructorLesson second = new ConstructorLesson("Lin", 3);
        first.award(2);
        assertEquals("Ada", first.name());
        assertEquals(2, first.points());
        assertEquals(3, second.points());
        assertEquals(before + 2, ConstructorLesson.created());
    }
    @Test public void testInvalidConstructionAndAwardLeaveStateValid() {
        assertThrows(NullPointerException.class, () -> new ConstructorLesson(null));
        assertThrows(IllegalArgumentException.class, () -> new ConstructorLesson("Ada", -1));
        final ConstructorLesson card = new ConstructorLesson("Ada", Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> card.award(-1));
        assertThrows(ArithmeticException.class, () -> card.award(1));
        assertEquals(Integer.MAX_VALUE, card.points());
    }
}
