import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/** Verifies state belongs to a particular object. @author Snehanjan Chatterjee */
public final class ScoreCardTest {
    @Test public void testIndependentInstancesAndSharedAlias() {
        final ScoreCard first = new ScoreCard();
        final ScoreCard second = new ScoreCard();
        final ScoreCard alias = first;
        alias.awardPoint();
        assertEquals(1, first.points());
        assertEquals(0, second.points());
        assertNotSame(first, second);
    }
}
