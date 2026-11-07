import java.util.Optional;
import java.util.function.Predicate;

/** Express small behaviors and absence without hiding side effects. @author Snehanjan Chatterjee */
public final class FunctionalValues {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Predicate<String> nonempty = value -> !value.isEmpty();
        final Optional<String> result = Optional.of(" java ").map(String::trim).filter(nonempty);
        System.out.println(result.orElseGet(() -> "unknown"));
        System.out.println(Optional.empty().orElse("fallback"));
    }
}
