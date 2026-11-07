import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Models validated alternatives and an array-backed record with explicit value semantics. @author Snehanjan Chatterjee */
public final class CoreRecordOwnership {
    sealed interface Result permits Found, Missing { }
    record Found(List<String> names) implements Result {
        Found { names = List.copyOf(names); }
    }
    record Missing(String key) implements Result {
        Missing { Objects.requireNonNull(key, "key"); }
    }
    record Sample(int[] values) {
        Sample { values = Objects.requireNonNull(values, "values").clone(); }
        /** Returns a detached component copy. */
        @Override public int[] values() { return values.clone(); }
        /** Compares array contents rather than generated reference equality. */
        @Override public boolean equals(final Object other) {
            return this == other || other instanceof Sample sample && Arrays.equals(values, sample.values);
        }
        /** Hashes the same contents used by equality. */
        @Override public int hashCode() { return Arrays.hashCode(values); }
    }
    /** Describes every declared alternative with an explicit null case. */
    static String describe(final Result result) {
        return switch (result) {
            case null -> "absent input";
            case Found(final List<String> names) -> "found=" + names.size();
            case Missing(final String key) -> "missing=" + key;
        };
    }
    /** Tests input/output ownership and alternative selection through deterministic output. */
    public static void main(final String[] args) {
        final int[] input = {1, 2};
        final Sample sample = new Sample(input);
        input[0] = 9; sample.values()[0] = 8;
        System.out.println(sample.values()[0]);
        System.out.println(sample.equals(new Sample(new int[] {1, 2})));
        System.out.println(describe(new Found(List.of("A", "B"))));
        System.out.println(describe(new Missing("x")));
        System.out.println(describe(null));
    }
}
