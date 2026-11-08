import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/** Distinguishes diagnostic names from identity and completed daemon work. @author Snehanjan Chatterjee */
public final class ThreadMetadataLessonTest {
    @Test public void testEqualNamesDoNotMergeThreadObjects() throws InterruptedException {
        assertArrayEquals(new String[] {"same name=true", "same object=false", "caller is first=false",
            "worker observed daemon=true", "worker finished=true"}, ThreadMetadataLesson.observations());
    }
}
