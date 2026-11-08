import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/** Normalizes machine tokens before first-appearance deduplication. @author Snehanjan Chatterjee */
public final class OrderedDeduplication {
    /** Rejects nulls, discards trim-empty tokens and returns a detached unmodifiable String sequence. */
    public static List<String> distinctTokens(final List<String> input) {
        Objects.requireNonNull(input, "input");
        final Set<String> unique = new LinkedHashSet<>();
        for (final String token : input) {
            final String normalized = Objects.requireNonNull(token, "token").trim().toLowerCase(Locale.ROOT);
            if (!normalized.isEmpty()) { unique.add(normalized); }
        }
        return Collections.unmodifiableList(new ArrayList<>(unique));
    }
    /** Shows normalization before membership and later caller edits not changing the snapshot. */
    public static void main(final String[] args) {
        final List<String> input = new ArrayList<>(Arrays.asList(" Java ", "JVM", "java", "", " jVm "));
        final List<String> distinct = distinctTokens(input);
        System.out.println(distinct);
        input.set(0, "changed");
        System.out.println(distinct);
    }
}
