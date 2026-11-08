import java.util.Objects;

/** Makes nullable wrapper policies explicit at primitive boundaries. */
public final class WrapperBoundariesLesson {
    /** Uses zero only when the API explicitly defines null as absence. */
    public static int orZero(final Integer value) { return value == null ? 0 : value; }
    /** Rejects absence before unboxing a required value. */
    public static int requireInt(final Integer value) {
        return Objects.requireNonNull(value, "value").intValue();
    }
    /** Contrasts parsing, boxing, typed equality and primitive comparison. */
    public static void main(final String[] args) {
        final int parsed = Integer.parseInt("42");
        final Integer boxed = Integer.valueOf("42");
        System.out.println(parsed);
        System.out.println(boxed.equals(Long.valueOf(42)));
        System.out.println(boxed == 42);
        System.out.println(orZero(null));
        System.out.println(requireInt(boxed));
    }
}
