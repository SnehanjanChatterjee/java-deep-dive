import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Build stable value contracts and prevent accidental state exposure. @author Snehanjan Chatterjee */
public final class ValueContracts {
    static final class Name {
        private final String value;
        Name(final String value) { this.value = Objects.requireNonNull(value); }
        @Override public boolean equals(final Object other) { return other instanceof Name && value.equals(((Name) other).value); }
        @Override public int hashCode() { return value.hashCode(); }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Set<Name> names = new HashSet<>();
        names.add(new Name("Mira"));
        names.add(new Name("Mira"));
        System.out.println(names.size());
    }
}
