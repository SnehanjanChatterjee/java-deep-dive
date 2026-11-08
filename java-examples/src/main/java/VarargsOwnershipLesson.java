import java.util.Objects;

/** Uses variable-arity calls without hiding array identity or overflow policy. */
public final class VarargsOwnershipLesson {
    /** Adds exactly; accepts zero values, rejects a null array and overflow. */
    public static int sum(final int... values) {
        Objects.requireNonNull(values, "values");
        int total = 0;
        for (final int value : values) { total = Math.addExact(total, value); }
        return total;
    }
    /** Returns an independent primitive array even for an explicit array call. */
    public static int[] snapshot(final int... values) {
        return Objects.requireNonNull(values, "values").clone();
    }
    /** Shows an empty call, a spread-looking call and an explicit array call. */
    public static void main(final String[] args) {
        System.out.println(sum());
        System.out.println(sum(2, 3, 4));
        final int[] source = {5, 6};
        final int[] copy = snapshot(source);
        copy[0] = 99;
        System.out.println(sum(source));
        System.out.println(source[0]);
        System.out.println(source == copy);
    }
}
