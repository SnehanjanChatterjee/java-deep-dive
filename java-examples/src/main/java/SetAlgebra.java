import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Computes membership results without mutating either input set. @author Snehanjan Chatterjee */
public final class SetAlgebra {
    /** Returns members appearing in exactly one input, retaining a deliberate encounter order. */
    public static <T> Set<T> symmetricDifference(final Set<? extends T> left, final Set<? extends T> right) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
        final Set<T> union = new LinkedHashSet<>(left);
        union.addAll(right);
        final Set<T> intersection = new LinkedHashSet<>(left);
        intersection.retainAll(right);
        union.removeAll(intersection);
        return union;
    }
    /** Compares union, intersection, difference and symmetric difference. */
    public static void main(final String[] args) {
        final Set<String> left = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));
        final Set<String> right = new LinkedHashSet<>(Arrays.asList("B", "D"));
        final Set<String> union = new LinkedHashSet<>(left); union.addAll(right);
        final Set<String> intersection = new LinkedHashSet<>(left); intersection.retainAll(right);
        final Set<String> difference = new LinkedHashSet<>(left); difference.removeAll(right);
        System.out.println("union=" + union);
        System.out.println("intersection=" + intersection);
        System.out.println("left-only=" + difference);
        System.out.println("exactly-one=" + symmetricDifference(left, right));
        System.out.println("inputs=" + left + "/" + right);
    }
}
