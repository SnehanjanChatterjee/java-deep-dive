import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks scalar reduction identity, type conversion and explicit invalid text. @author Snehanjan Chatterjee */
public final class ReductionLawsTest {
    @Test public void testLengthAndEmptyIdentity() {
        assertEquals(8L, ReductionLaws.totalLength(Arrays.asList("Java", "JVM", "a")));
        assertEquals(0L, ReductionLaws.totalLength(Collections.<String>emptyList()));
        assertEquals(0L, ReductionLaws.totalLength(Arrays.asList("", "")));
    }
    @Test public void testInvalidInputIsNotConvertedToEmpty() {
        assertThrows(NullPointerException.class, () -> ReductionLaws.totalLength(null));
        assertThrows(NullPointerException.class, () -> ReductionLaws.totalLength(Arrays.asList("a", null)));
    }
}
