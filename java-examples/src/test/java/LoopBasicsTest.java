import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks loop termination and the declared arithmetic domain. @author Snehanjan Chatterjee */
public final class LoopBasicsTest {
    @Test public void testEmptySingleAndSeveralIterations() {
        assertEquals(0, LoopBasics.sumThrough(0));
        assertEquals(1, LoopBasics.sumThrough(1));
        assertEquals(10, LoopBasics.sumThrough(4));
        assertEquals(2_147_450_880, LoopBasics.sumThrough(65_535));
    }
    @Test public void testRejectInvalidLimits() {
        assertThrows(IllegalArgumentException.class, () -> LoopBasics.sumThrough(-1));
        assertThrows(IllegalArgumentException.class, () -> LoopBasics.sumThrough(65_536));
    }
}
