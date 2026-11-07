import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/** Checks record constructor invariants and array value ownership. @author Snehanjan Chatterjee */
public final class RecordValueLessonTest {
    @Test public void testRangeValidation() {
        final RecordValueLesson.Range range = new RecordValueLesson.Range(2, 5);
        assertEquals(2, range.start()); assertEquals(5, range.end());
        assertEquals(2, new RecordValueLesson.Range(2, 2).end());
        assertThrows(IllegalArgumentException.class, () -> new RecordValueLesson.Range(5, 2));
    }
    @Test public void testSampleCopiesBothBoundariesAndComparesContents() {
        final int[] input = {1, 2};
        final RecordValueLesson.Sample sample = new RecordValueLesson.Sample(input);
        input[0] = 9; sample.values()[0] = 8;
        assertEquals(1, sample.values()[0]);
        final RecordValueLesson.Sample equal = new RecordValueLesson.Sample(new int[] {1, 2});
        assertEquals(sample, sample); assertEquals(sample, equal); assertEquals(sample.hashCode(), equal.hashCode());
        assertFalse(sample.equals(new RecordValueLesson.Sample(new int[] {2, 1})));
        assertFalse(sample.equals(null)); assertFalse(sample.equals("other"));
        assertEquals(0, new RecordValueLesson.Sample(new int[0]).values().length);
        assertThrows(NullPointerException.class, () -> new RecordValueLesson.Sample(null));
    }
}
