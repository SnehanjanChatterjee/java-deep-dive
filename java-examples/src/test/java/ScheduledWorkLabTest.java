import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Checks result-state and exception policy without timing assertions. @author Snehanjan Chatterjee */
public final class ScheduledWorkLabTest {
    @Test public void testPeriodicCancellation() throws InterruptedException { assertTrue(ScheduledWorkLab.awaitTicksAndCancel()); }
    @Test public void testPeriodicFailureSuppressesLaterRuns() throws Exception { assertEquals(1, ScheduledWorkLab.periodicFailureRuns()); }
}
