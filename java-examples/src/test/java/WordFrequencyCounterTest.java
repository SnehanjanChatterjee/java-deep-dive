import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class WordFrequencyCounterTest {
    @Test public void testRepeatedWordsAndDistinctValues() {
        final Map<String, Integer> actual = WordFrequencyCounter.count(new String[] {"java", "jvm", "java"});
        assertEquals(Integer.valueOf(2), actual.get("java"));
        assertEquals(Integer.valueOf(1), actual.get("jvm"));
        assertEquals(2, actual.size());
    }
    @Test public void testEmptyInput() { assertTrue(WordFrequencyCounter.count(new String[0]).isEmpty()); }
    @Test public void testCollidingUnequalWordsRemainSeparate() {
        assertEquals("FB".hashCode(), "Ea".hashCode());
        final Map<String, Integer> actual = WordFrequencyCounter.count(new String[] {"FB", "Ea", "FB"});
        assertEquals(Integer.valueOf(2), actual.get("FB"));
        assertEquals(Integer.valueOf(1), actual.get("Ea"));
    }
    @Test public void testCaseAndEmptyWordAreNotNormalized() {
        final Map<String, Integer> actual = WordFrequencyCounter.count(new String[] {"Java", "java", ""});
        assertEquals(3, actual.size());
        assertEquals(Integer.valueOf(1), actual.get(""));
    }
    @Test(expected = NullPointerException.class) public void testNullArrayRejected() { WordFrequencyCounter.count(null); }
    @Test(expected = NullPointerException.class) public void testNullElementRejected() { WordFrequencyCounter.count(new String[] {"java", null}); }
}
