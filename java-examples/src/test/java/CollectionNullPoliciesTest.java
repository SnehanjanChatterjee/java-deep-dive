import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeMap;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Separates null acceptance from a duplicate set add and unsupported mutation. @author Snehanjan Chatterjee */
public final class CollectionNullPoliciesTest {
    @Test public void testAcceptedAndRejectedNulls() {
        assertTrue(CollectionNullPolicies.acceptsNull(new ArrayList<String>()));
        final HashSet<String> set = new HashSet<>();
        assertTrue(CollectionNullPolicies.acceptsNull(set));
        assertTrue(CollectionNullPolicies.acceptsNull(set));
        assertTrue(CollectionNullPolicies.acceptsNull(new HashMap<String, Integer>(), true));
        assertTrue(CollectionNullPolicies.acceptsNull(new HashMap<String, Integer>(), false));
        assertFalse(CollectionNullPolicies.acceptsNull(new TreeMap<String, Integer>(), true));
        assertTrue(CollectionNullPolicies.acceptsNull(new TreeMap<String, Integer>(), false));
        assertFalse(CollectionNullPolicies.acceptsNull(new Hashtable<String, Integer>(), false));
    }
    @Test public void testInvalidContainerAndDifferentFailure() {
        assertThrows(NullPointerException.class, () -> CollectionNullPolicies.acceptsNull(null));
        assertThrows(NullPointerException.class, () -> CollectionNullPolicies.acceptsNull(null, true));
        assertThrows(UnsupportedOperationException.class, () -> CollectionNullPolicies.acceptsNull(Collections.<String>emptyList()));
    }
}
