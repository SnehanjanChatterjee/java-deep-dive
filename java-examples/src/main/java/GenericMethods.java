import java.util.NoSuchElementException;
import java.util.Objects;

/** Connects a generic method's input and return type. @author Snehanjan Chatterjee */
public final class GenericMethods {
    /** Returns the supplied reference unchanged, including null. */
    public static <T> T identity(final T value) { return value; }
    /** Returns the first component, rejecting a null or empty array. */
    public static <T> T first(final T[] values) {
        Objects.requireNonNull(values, "values");
        if (values.length == 0) { throw new NoSuchElementException("empty array"); }
        return values[0];
    }
    /** Shows inference and an explicit method type argument. */
    public static void main(final String[] args) {
        final String text = identity("Java");
        final Integer number = GenericMethods.<Integer>identity(7);
        System.out.println(text);
        System.out.println(number);
        System.out.println(first(new String[] {"Ada", "Lin"}));
    }
}
