import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks one participant's interrupted arrival breaks the generation for peers. @author Snehanjan Chatterjee */
public final class BarrierFailureLessonTest {
    @Test(timeout = 5000) public void testInterruptedArrivalBreaksGeneration() throws InterruptedException {
        assertArrayEquals(new String[] {"interrupted status=false", "broken=true", "peer saw broken generation"},
            BarrierFailureLesson.observations());
    }
}
