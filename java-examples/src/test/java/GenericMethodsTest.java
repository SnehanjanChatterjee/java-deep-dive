import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/** Checks generic input/result relationships and array failure contracts. @author Snehanjan Chatterjee */
public final class GenericMethodsTest {
    @Test public void testReturnsExistingReferenceIncludingNull() {
        final Object value = new Object();
        assertSame(value, GenericMethods.identity(value));
        assertSame(value, GenericMethods.first(new Object[] {value, new Object()}));
        assertNull(GenericMethods.identity(null));
        assertNull(GenericMethods.first(new String[] {null}));
    }
    @Test public void testRejectNullAndEmptyArray() {
        assertThrows(NullPointerException.class, () -> GenericMethods.first(null));
        assertThrows(NoSuchElementException.class, () -> GenericMethods.first(new String[0]));
    }
}
