import java.util.Optional;

/** Separates a missing configuration value from invalid present input. @author Snehanjan Chatterjee */
public final class CoreOptionalBoundary {
    /** Returns absence only for null; present input must be an integer in 1..65535. */
    public static Optional<Integer> parsePort(final String input) {
        return Optional.ofNullable(input).map(CoreOptionalBoundary::parsePresent);
    }
    /** Validates one present representation without swallowing malformed input. */
    private static int parsePresent(final String input) {
        final int value = Integer.parseInt(input);
        if (value < 1 || value > 65535) { throw new IllegalArgumentException("port outside 1..65535"); }
        return value;
    }
    /** Shows present, absent and invalid outcomes separately. */
    public static void main(final String[] args) {
        System.out.println(parsePort("443").orElse(8080));
        System.out.println(parsePort(null).orElse(8080));
        try { parsePort("0"); }
        catch (final IllegalArgumentException expected) { System.out.println("invalid port"); }
    }
}
