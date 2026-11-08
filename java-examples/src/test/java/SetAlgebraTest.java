import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks membership, order policy and absence of input mutation. @author Snehanjan Chatterjee */
public final class SetAlgebraTest {
    @Test public void testDisjointOverlapAndIdenticalSets() {
        final Set<String> left = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));
        final Set<String> right = new LinkedHashSet<>(Arrays.asList("B", "D"));
        assertEquals(Arrays.asList("A", "C", "D"), new ArrayList<>(SetAlgebra.symmetricDifference(left, right)));
        assertEquals(Arrays.asList("A", "B", "C"), new ArrayList<>(left));
        assertEquals(Arrays.asList("B", "D"), new ArrayList<>(right));
        assertEquals(Collections.emptySet(), SetAlgebra.symmetricDifference(left, left));
        assertEquals(left, SetAlgebra.symmetricDifference(left, Collections.<String>emptySet()));
    }
    @Test public void testNullMembershipAndInvalidContainers() {
        final Set<String> left = new LinkedHashSet<>(Arrays.asList(null, "A"));
        final Set<String> right = new LinkedHashSet<>(Arrays.asList(null, "B"));
        assertEquals(new LinkedHashSet<>(Arrays.asList("A", "B")), SetAlgebra.symmetricDifference(left, right));
        assertThrows(NullPointerException.class, () -> SetAlgebra.symmetricDifference(null, right));
        assertThrows(NullPointerException.class, () -> SetAlgebra.symmetricDifference(left, null));
    }
}
