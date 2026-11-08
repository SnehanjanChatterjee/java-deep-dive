import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks the declared filtering, uniqueness, ranking and slicing result. @author Snehanjan Chatterjee */
public final class StreamOperationsTest {
    @Test public void testRankingAndInputPreservation() {
        final List<Integer> input = Arrays.asList(3, -1, 3, 1, 4, null, 2);
        assertEquals(Arrays.asList(3, 2), StreamOperations.ranked(input));
        assertEquals(Arrays.asList(3, -1, 3, 1, 4, null, 2), input);
        assertEquals(Collections.emptyList(), StreamOperations.ranked(Arrays.asList(null, -1, 0)));
        assertEquals(Collections.emptyList(), StreamOperations.ranked(Collections.singletonList(7)));
        assertEquals(Collections.emptyList(), StreamOperations.ranked(Collections.<Integer>emptyList()));
    }
    @Test public void testNullContainerRejected() {
        assertThrows(NullPointerException.class, () -> StreamOperations.ranked(null));
    }
}
