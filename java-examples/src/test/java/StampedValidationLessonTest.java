import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks an intervening write forces selection of the fallback read. @author Snehanjan Chatterjee */
public final class StampedValidationLessonTest {
    @Test public void testInvalidTentativeValueIsDiscarded() {
        assertArrayEquals(new String[] {"tentative=0", "valid=false", "selected=42"},
            StampedValidationLesson.observations());
    }
}
