import java.util.Objects;

/** Defines value equality without requiring collection APIs. @author Snehanjan Chatterjee */
public final class EqualityBasics {
    static final class Name {
        private final String value;
        /** Requires the immutable String that defines this value. */
        Name(final String value) { this.value = Objects.requireNonNull(value, "value"); }
        /** Compares the same meaningful state for every Name. */
        @Override public boolean equals(final Object other) {
            return this == other || other instanceof Name && value.equals(((Name) other).value);
        }
        /** Hashes the state used for equality. */
        @Override public int hashCode() { return value.hashCode(); }
    }
    /** Compares two independently constructed equal values. */
    public static void main(final String[] args) {
        final Name first = new Name("Ada");
        final Name second = new Name("Ada");
        System.out.println(first == second);
        System.out.println(first.equals(second));
        System.out.println(first.hashCode() == second.hashCode());
    }
}
