import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Selects built-in collector result shapes over one small immutable input model. @author Snehanjan Chatterjee */
public final class CollectorCatalog {
    public static final class Score {
        private final String name;
        private final String team;
        private final int points;
        /** Establishes non-null labels and nonnegative points for this example. */
        public Score(final String name, final String team, final int points) {
            this.name = Objects.requireNonNull(name, "name");
            this.team = Objects.requireNonNull(team, "team");
            if (points < 0) { throw new IllegalArgumentException("negative points"); }
            this.points = points;
        }
        /** Returns the participant label. */
        public String name() { return name; }
        /** Returns the grouping label. */
        public String team() { return team; }
        /** Returns the input points. */
        public int points() { return points; }
    }
    /** Combines repeated participant labels with exact arithmetic and sorted output keys. */
    public static Map<String, Integer> totalsByName(final List<Score> values) {
        Objects.requireNonNull(values, "values");
        return values.stream().collect(Collectors.toMap(Score::name, Score::points, Math::addExact, TreeMap::new));
    }
    /** Maps each team's inputs to names, preserving encounter order within each group. */
    public static Map<String, List<String>> namesByTeam(final List<Score> values) {
        Objects.requireNonNull(values, "values");
        return values.stream().collect(Collectors.groupingBy(Score::team, TreeMap::new,
                Collectors.mapping(Score::name, Collectors.toList())));
    }
    /** Finishes a private list accumulator into an unmodifiable structural snapshot. */
    public static List<String> readOnlyNames(final List<Score> values) {
        Objects.requireNonNull(values, "values");
        return values.stream().map(Score::name).collect(
                Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
    /** Uses collect's three-function form with fresh mutable state and a compatible combiner. */
    public static String concatenate(final List<String> values) {
        Objects.requireNonNull(values, "values");
        final StringBuilder result = values.stream().collect(StringBuilder::new,
                (buffer, text) -> buffer.append(Objects.requireNonNull(text, "text")), StringBuilder::append);
        return result.toString();
    }
    /** Exercises collections, joining, grouping, partitioning and numeric collector families. */
    public static void main(final String[] args) {
        final List<Score> values = Arrays.asList(new Score("Ada", "red", 100),
                new Score("Bob", "blue", 250), new Score("Ada", "red", 100));
        final List<String> names = values.stream().map(Score::name).collect(Collectors.toList());
        final Set<String> unique = values.stream().map(Score::name).collect(Collectors.toSet());
        final LinkedHashSet<String> ordered = values.stream().map(Score::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("list=" + names + ", distinct count=" + unique.size());
        System.out.println("specified collection=" + ordered);
        System.out.println("joining=" + values.stream().map(Score::name).collect(Collectors.joining("|", "[", "]")));
        System.out.println("count=" + values.stream().collect(Collectors.counting()));
        final int intSum = values.stream().collect(Collectors.summingInt(Score::points));
        final long longSum = values.stream().collect(Collectors.summingLong(Score::points));
        final double doubleSum = values.stream().collect(Collectors.summingDouble(Score::points));
        System.out.println("sums=" + intSum + "/" + longSum + "/" + doubleSum);
        System.out.println("averages=" + values.stream().collect(Collectors.averagingInt(Score::points))
                + "/" + values.stream().collect(Collectors.averagingLong(Score::points))
                + "/" + values.stream().collect(Collectors.averagingDouble(Score::points)));
        final IntSummaryStatistics stats = values.stream().collect(Collectors.summarizingInt(Score::points));
        System.out.println("summary=" + stats.getCount() + "/" + stats.getSum() + "/" + stats.getMin() + "/" + stats.getMax());
        System.out.println("long/double summary sum=" + values.stream().collect(Collectors.summarizingLong(Score::points)).getSum()
                + "/" + values.stream().collect(Collectors.summarizingDouble(Score::points)).getSum());
        System.out.println("min/max=" + values.stream().collect(Collectors.minBy(Comparator.comparingInt(Score::points)))
                .map(Score::name).orElse("none") + "/" + values.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(Score::points))).map(Score::name).orElse("none"));
        final int reduced = values.stream().collect(Collectors.reducing(0, Score::points, Integer::sum));
        System.out.println("reducing=" + reduced);
        System.out.println("toMap=" + totalsByName(values));
        System.out.println("group mapping=" + namesByTeam(values));
        final Map<Boolean, List<String>> partitions = new TreeMap<>(values.stream().collect(
                Collectors.partitioningBy(score -> score.points() >= 200,
                        Collectors.mapping(Score::name, Collectors.toList()))));
        System.out.println("partitioning=" + partitions);
        final List<String> snapshot = readOnlyNames(new ArrayList<>(values));
        try { snapshot.add("invalid"); throw new IllegalStateException("expected unmodifiable list"); }
        catch (final UnsupportedOperationException expected) { System.out.println("finisher mutation rejected"); }
    }
}
