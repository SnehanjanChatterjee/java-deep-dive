import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Keeps literal input safe in both the regex pattern and replacement languages. */
public final class RegexLiteralLesson {
    /** Replaces literal occurrences using an explicitly quoted regex operation. */
    public static String replaceLiteral(final String input, final String needle, final String replacement) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(needle, "needle");
        Objects.requireNonNull(replacement, "replacement");
        return Pattern.compile(Pattern.quote(needle)).matcher(input)
                .replaceAll(Matcher.quoteReplacement(replacement));
    }
    /** Demonstrates metacharacters and split's trailing-empty policy. */
    public static void main(final String[] args) {
        System.out.println(replaceLiteral("a.b.a", ".", "$1"));
        System.out.println("a,b,".split(",").length);
        System.out.println("a,b,".split(",", -1).length);
    }
}
