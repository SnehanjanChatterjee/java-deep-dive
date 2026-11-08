import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Uses named ordering policies before introducing lambda shorthand. @author Snehanjan Chatterjee */
public final class CollectionOrdering {
    public static final class Score implements Comparable<Score> {
        private final String id;
        private final int points;
        /** Creates a score with a non-null stable identifier. */
        public Score(final String id, final int points) {
            this.id = Objects.requireNonNull(id, "id");
            this.points = points;
        }
        /** Exposes the immutable identifier to external ordering policies. */
        public String id() { return id; }
        /** Exposes the primitive comparison key without a mutation API. */
        public int points() { return points; }
        /** Defines the natural order by identifier, then points. */
        @Override public int compareTo(final Score other) {
            final int byId = id.compareTo(other.id);
            return byId != 0 ? byId : Integer.compare(points, other.points);
        }
        /** Defines value equality using the same two fields as either ordering. */
        @Override public boolean equals(final Object other) {
            if (this == other) { return true; }
            if (!(other instanceof Score)) { return false; }
            final Score score = (Score) other;
            return points == score.points && id.equals(score.id);
        }
        /** Hashes the fields used by equality. */
        @Override public int hashCode() { return 31 * id.hashCode() + Integer.hashCode(points); }
        /** Renders both fields to make the ordering decision visible. */
        @Override public String toString() { return id + ":" + points; }
    }
    public static final class ByPointsThenId implements Comparator<Score> {
        /** Orders by points, breaking ties by identifier without subtraction. */
        @Override public int compare(final Score left, final Score right) {
            final int byPoints = Integer.compare(left.points, right.points);
            return byPoints != 0 ? byPoints : left.id.compareTo(right.id);
        }
    }
    /** Shows one natural order and one external policy over the same objects. */
    public static void main(final String[] args) {
        final List<Score> scores = new ArrayList<>(Arrays.asList(
                new Score("C", 10), new Score("A", 20), new Score("B", 10)));
        Collections.sort(scores);
        System.out.println(scores);
        Collections.sort(scores, new ByPointsThenId());
        System.out.println(scores);
    }
}
