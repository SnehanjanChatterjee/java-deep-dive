import java.util.Optional;
import java.util.OptionalInt;

/** Labels Optional conveniences added after its original Java 8 API. @author Snehanjan Chatterjee */
public final class OptionalOperations {
    /** Exercises Java 9 alternatives/stream, Java 10 retrieval and Java 11 absence query. */
    public static void main(final String[] args) {
        final Optional<String> present = Optional.of("primary");
        final Optional<String> absent = Optional.empty();
        System.out.println(absent.or(() -> Optional.of("secondary")).orElseThrow());
        absent.ifPresentOrElse(System.out::println, () -> System.out.println("empty branch"));
        System.out.println("absent=" + absent.isEmpty());
        System.out.println("stream sizes=" + present.stream().count() + "/" + absent.stream().count());
        System.out.println("primitive sum=" + OptionalInt.of(7).stream().sum());
    }
}
