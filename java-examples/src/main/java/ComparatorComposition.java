import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** Reverses either one comparison key or the complete lexicographic policy. @author Snehanjan Chatterjee */
public final class ComparatorComposition {
    /** Shows how placement of reversed changes tie order. */
    public static void main(final String[] args) {
        final List<CollectionOrdering.Score> values = new ArrayList<>(Arrays.asList(
                new CollectionOrdering.Score("C", 10), new CollectionOrdering.Score("A", 20),
                new CollectionOrdering.Score("B", 10)));
        final Comparator<CollectionOrdering.Score> ascending = Comparator.comparingInt(CollectionOrdering.Score::points)
                .thenComparing(CollectionOrdering.Score::id);
        values.sort(ascending);
        System.out.println("ascending=" + values);
        values.sort(ascending.reversed());
        System.out.println("both keys reversed=" + values);
        values.sort(Comparator.comparingInt(CollectionOrdering.Score::points).reversed()
                .thenComparing(CollectionOrdering.Score::id));
        System.out.println("points only reversed=" + values);
    }
}
