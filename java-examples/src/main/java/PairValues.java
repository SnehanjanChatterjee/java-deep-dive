import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Gives a composite value its own equality rather than encoding it as text. @author Snehanjan Chatterjee */
public final class PairValues {
    public static final class Pair<A, B> {
        private final A first;
        private final B second;
        /** Stores two references; null components are allowed by this value contract. */
        public Pair(final A first, final B second) { this.first = first; this.second = second; }
        /** Returns the first component without claiming it is deeply immutable. */
        public A first() { return first; }
        /** Returns the second component. */
        public B second() { return second; }
        /** Compares corresponding components, preserving order within the pair. */
        @Override public boolean equals(final Object other) {
            if (this == other) { return true; }
            if (!(other instanceof Pair<?, ?>)) { return false; }
            final Pair<?, ?> pair = (Pair<?, ?>) other;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }
        /** Hashes exactly the components used by equality. */
        @Override public int hashCode() { return Objects.hash(first, second); }
        /** Produces diagnostics, not a collision-free serialized key. */
        @Override public String toString() { return "(" + first + ", " + second + ")"; }
    }
    /** Shows duplicate values, component order and ambiguous text encodings. */
    public static void main(final String[] args) {
        final Set<Pair<Integer, Integer>> pairs = new HashSet<>();
        System.out.println(pairs.add(new Pair<>(3, 5)));
        System.out.println(pairs.add(new Pair<>(3, 5)));
        System.out.println(pairs.add(new Pair<>(5, 3)));
        System.out.println("size=" + pairs.size());
        final Pair<String, String> left = new Pair<>("a,b", "c");
        final Pair<String, String> right = new Pair<>("a", "b,c");
        System.out.println("text collides=" + (left.first() + "," + left.second()).equals(right.first() + "," + right.second()));
        System.out.println("pairs equal=" + left.equals(right));
    }
}
