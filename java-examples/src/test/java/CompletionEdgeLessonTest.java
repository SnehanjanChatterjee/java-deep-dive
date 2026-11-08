import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Checks aggregate identities and exceptional null composition without an endless wait. @author Snehanjan Chatterjee */
public final class CompletionEdgeLessonTest {
    @Test public void testEmptyAggregatesAndNullDependentStage() {
        assertArrayEquals(new String[] {"empty all done=true", "empty all value=null",
            "empty any done=false", "null composition=NullPointerException"}, CompletionEdgeLesson.observations());
    }
}
