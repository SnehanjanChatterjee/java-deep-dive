import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Counts already tokenized words with ordinary map lookup and replacement. @author Snehanjan Chatterjee */
public final class WordFrequencyCounter {
    /** Counts non-null words using exact String equality, without changing the supplied array. */
    public static Map<String, Integer> count(final String[] words) {
        Objects.requireNonNull(words, "words");
        final Map<String, Integer> counts = new HashMap<>();
        for (final String word : words) {
            Objects.requireNonNull(word, "word");
            final Integer previous = counts.get(word);
            counts.put(word, previous == null ? 1 : previous + 1);
        }
        return counts;
    }

    /** Prints two counts without relying on map iteration order. */
    public static void main(final String[] args) {
        final Map<String, Integer> counts = count(new String[] {"java", "jvm", "java"});
        System.out.println(counts.get("java"));
        System.out.println(counts.get("jvm"));
    }
}
