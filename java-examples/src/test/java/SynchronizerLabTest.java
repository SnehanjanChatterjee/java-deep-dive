import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks deterministic result publication through latch completion. @author Snehanjan Chatterjee */
public final class SynchronizerLabTest {
    @Test public void testPublishedResultsAfterCompletion() throws InterruptedException { assertEquals(41, SynchronizerLab.sum()); }
}
