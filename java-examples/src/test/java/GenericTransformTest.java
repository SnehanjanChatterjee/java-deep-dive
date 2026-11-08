import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks a two-type transformation and caller-visible failure boundaries. @author Snehanjan Chatterjee */
public final class GenericTransformTest {
    @Test public void testDifferentResultTypeEmptyAndSourceOwnership() {
        final String[] values = {"Java", "JVM"};
        assertEquals(Arrays.asList(4, 3), GenericTransform.transform(values, new GenericTransform.StringLength()));
        assertArrayEquals(new String[] {"Java", "JVM"}, values);
        assertEquals(Collections.emptyList(), GenericTransform.transform(new String[0], new GenericTransform.StringLength()));
    }
    @Test public void testNullInputOperationAndElement() {
        assertThrows(NullPointerException.class, () -> GenericTransform.transform(null, new GenericTransform.StringLength()));
        assertThrows(NullPointerException.class, () -> GenericTransform.transform(new String[] {"a"}, null));
        final String[] values = {"a", null};
        assertThrows(NullPointerException.class, () -> GenericTransform.transform(values, new GenericTransform.StringLength()));
        assertArrayEquals(new String[] {"a", null}, values);
    }
}
