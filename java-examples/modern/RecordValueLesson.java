import java.util.Arrays;
import java.util.Objects;

/** Shows record validation and array ownership without generic collections. @author Snehanjan Chatterjee */
public final class RecordValueLesson {
    record Range(int start, int end) {
        Range { if (start > end) { throw new IllegalArgumentException("start exceeds end"); } }
    }
    record Sample(int[] values) {
        Sample { values = Objects.requireNonNull(values, "values").clone(); }
        /** Returns a detached component copy. */
        @Override public int[] values() { return values.clone(); }
        /** Uses array contents rather than generated array identity equality. */
        @Override public boolean equals(final Object other) {
            return this == other || other instanceof Sample && Arrays.equals(values, ((Sample) other).values);
        }
        /** Hashes the same array contents. */
        @Override public int hashCode() { return Arrays.hashCode(values); }
    }
    /** Shows generated component access and custom ownership/value semantics. */
    public static void main(final String[] args) {
        final Range range = new Range(2, 5);
        System.out.println(range.start() + ":" + range.end());
        final int[] input = {1, 2};
        final Sample sample = new Sample(input);
        input[0] = 9; sample.values()[0] = 8;
        System.out.println(sample.values()[0]);
        System.out.println(sample.equals(new Sample(new int[] {1, 2})));
    }
}
