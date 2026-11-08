import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks normalized first appearance and caller/result ownership. @author Snehanjan Chatterjee */
public final class OrderedDeduplicationTest {
    @Test public void testCanonicalOrderBlankAndSnapshot() {
        final List<String> input = new ArrayList<>(Arrays.asList(" Java ", "JVM", "java", " ", " jVm "));
        final List<String> result = OrderedDeduplication.distinctTokens(input);
        assertEquals(Arrays.asList("java", "jvm"), result);
        input.clear(); assertEquals(Arrays.asList("java", "jvm"), result);
        assertThrows(UnsupportedOperationException.class, () -> result.add("x"));
        assertEquals(Collections.emptyList(), OrderedDeduplication.distinctTokens(Collections.<String>emptyList()));
    }
    @Test public void testNullsAreInvalidInsteadOfSilentlyDropped() {
        assertThrows(NullPointerException.class, () -> OrderedDeduplication.distinctTokens(null));
        assertThrows(NullPointerException.class, () -> OrderedDeduplication.distinctTokens(Arrays.asList("x", null)));
    }
}
