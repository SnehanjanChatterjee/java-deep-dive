import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks supported removal and argument boundaries. @author Snehanjan Chatterjee */
public final class CollectionIterationTest {
    @Test public void testRemovalAndEmpty() {
        final ArrayList<String> names = new ArrayList<>(Arrays.asList("Al", "Mira", "Bo", "Noor"));
        CollectionIteration.removeShortNames(names, 3);
        assertEquals(Arrays.asList("Mira", "Noor"), names);
        CollectionIteration.removeShortNames(names, 0);
        assertEquals(2, names.size());
        CollectionIteration.removeShortNames(names, 5);
        assertEquals(Collections.emptyList(), names);
        CollectionIteration.removeShortNames(names, 2);
        assertEquals(0, names.size());
    }
    @Test public void testInvalidArguments() {
        assertThrows(NullPointerException.class, () -> CollectionIteration.removeShortNames(null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> CollectionIteration.removeShortNames(new ArrayList<String>(), -1));
    }
}
