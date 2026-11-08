import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Separates account absence, address absence and invalid lookup input. @author Snehanjan Chatterjee */
public final class OptionalCompositionTest {
    @Test public void testPresentAndTwoAbsencePaths() {
        assertTrue(OptionalComposition.find("known").isPresent());
        assertEquals("Main Street", OptionalComposition.findAddress("known").get());
        assertTrue(OptionalComposition.find("no-address").isPresent());
        assertFalse(OptionalComposition.findAddress("no-address").isPresent());
        assertFalse(OptionalComposition.find("missing").isPresent());
        assertFalse(OptionalComposition.findAddress("missing").isPresent());
    }
    @Test public void testNullLookupRejected() {
        assertThrows(NullPointerException.class, () -> OptionalComposition.find(null));
        assertThrows(NullPointerException.class, () -> OptionalComposition.findAddress(null));
    }
}
