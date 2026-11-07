import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Separates searching for occurrences from whole-input matching. @author Snehanjan Chatterjee */
public final class DigitSearch {
    /** Prints numeric occurrences and then a whole-input validation result. */
    public static void main(final String[] args) {
        final Pattern digits = Pattern.compile("\\d+");
        final Matcher matcher = digits.matcher("item 12, item 7");
        while (matcher.find()) { System.out.println(matcher.group()); }
        System.out.println("item 12".matches("\\d+"));
    }
}
