import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Builds the same value transformation using a loop and a stream pipeline. @author Snehanjan Chatterjee */
public final class StreamFoundationsLab {
    /** Runs a filter/map comparison and then flattens nested lists. */
    public static void main(final String[] args) {
        final List<String> words = Arrays.asList("java", "a", "jvm");
        final List<String> loop = new ArrayList<>();
        for (final String word : words) {
            if (word.length() >= 3) { loop.add(word.toUpperCase(Locale.ROOT)); }
        }
        System.out.println(loop);
        final List<String> pipeline = words.stream().filter(word -> word.length() >= 3)
                .map(word -> word.toUpperCase(Locale.ROOT)).collect(Collectors.toList());
        System.out.println(pipeline);
        final List<List<String>> groups = Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("C"));
        System.out.println(groups.stream().flatMap(List::stream).collect(Collectors.toList()));
        System.out.println(Arrays.asList(1, 2, 3, 4).stream().filter(value -> value % 2 == 0)
                .map(value -> value * value).findFirst().orElse(-1));
    }
}
