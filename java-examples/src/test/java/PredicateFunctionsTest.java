import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks encounter order, empty selection and the null-guard's actual invocation count. @author Snehanjan Chatterjee */
public final class PredicateFunctionsTest {
    @Test public void testSelectionAndIndependentResult() {
        final List<String> values = Arrays.asList("a", "Java", "JVM");
        final List<String> result = PredicateFunctions.select(values, text -> text.length() >= 3);
        assertEquals(Arrays.asList("Java", "JVM"), result);
        result.clear(); assertEquals(Arrays.asList("a", "Java", "JVM"), values);
        assertEquals(Collections.emptyList(), PredicateFunctions.select(Collections.<String>emptyList(), text -> true));
        assertEquals(2, PredicateFunctions.guardedLengthChecks(Arrays.asList(null, "a", "Java")));
        assertEquals(0, PredicateFunctions.guardedLengthChecks(Arrays.asList(null, null)));
    }
    @Test public void testInvalidBoundariesAndPredicateFailure() {
        assertThrows(NullPointerException.class, () -> PredicateFunctions.select(null, text -> true));
        assertThrows(NullPointerException.class, () -> PredicateFunctions.select(Collections.emptyList(), null));
        assertThrows(NullPointerException.class, () -> PredicateFunctions.guardedLengthChecks(null));
        assertThrows(IllegalStateException.class,
                () -> PredicateFunctions.select(Arrays.asList("x"), text -> { throw new IllegalStateException("rule failure"); }));
    }
}
