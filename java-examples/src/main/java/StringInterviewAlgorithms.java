import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

/** Implements code-point-based string exercises, not grapheme-cluster editing. @author Snehanjan Chatterjee */
public final class StringInterviewAlgorithms {
    /** Reverses Unicode code points while preserving each surrogate pair. */
    public static String reverseCodePoints(final String input) {
        Objects.requireNonNull(input, "input");
        final StringBuilder reversed = new StringBuilder(input.length());
        for (int end = input.length(); end > 0;) {
            final int point = input.codePointBefore(end);
            reversed.appendCodePoint(point);
            end -= Character.charCount(point);
        }
        return reversed.toString();
    }

    /** Returns the first code point occurring exactly once, or absence. */
    public static OptionalInt firstUnique(final String input) {
        Objects.requireNonNull(input, "input");
        final Map<Integer, Integer> counts = new LinkedHashMap<>();
        for (int offset = 0; offset < input.length();) {
            final int point = input.codePointAt(offset);
            final Integer previous = counts.get(point);
            counts.put(point, previous == null ? 1 : previous + 1);
            offset += Character.charCount(point);
        }
        for (final Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 1) { return OptionalInt.of(entry.getKey()); }
        }
        return OptionalInt.empty();
    }

    /** Removes only trailing Character.isWhitespace code points; preserves leading text. */
    public static String stripTrailingWhitespace(final String input) {
        Objects.requireNonNull(input, "input");
        int end = input.length();
        while (end > 0) {
            final int point = input.codePointBefore(end);
            if (!Character.isWhitespace(point)) { break; }
            end -= Character.charCount(point);
        }
        return input.substring(0, end);
    }

    /** Demonstrates supplementary characters and first-unique semantics. */
    public static void main(final String[] args) {
        System.out.println(reverseCodePoints("A\uD83D\uDE00B").equals("B\uD83D\uDE00A"));
        System.out.println(firstUnique("swiss").getAsInt() == 'w');
        System.out.println(firstUnique("aabb").isPresent());
    }
}
