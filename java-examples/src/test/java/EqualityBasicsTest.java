import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/** Checks the value relation across independent objects. @author Snehanjan Chatterjee */
public final class EqualityBasicsTest {
    @Test public void testEqualityLawsAndMatchingHashes() {
        final EqualityBasics.Name a = new EqualityBasics.Name("Ada");
        final EqualityBasics.Name b = new EqualityBasics.Name("Ada");
        final EqualityBasics.Name c = new EqualityBasics.Name("Ada");
        assertTrue(a.equals(a)); assertTrue(a.equals(b)); assertTrue(b.equals(a));
        assertTrue(b.equals(c)); assertTrue(a.equals(c)); assertEquals(a.hashCode(), b.hashCode());
        assertFalse(a.equals(null)); assertFalse(a.equals("Ada"));
        assertFalse(a.equals(new EqualityBasics.Name("Lin")));
        assertThrows(NullPointerException.class, () -> new EqualityBasics.Name(null));
    }
}
