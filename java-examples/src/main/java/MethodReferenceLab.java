import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/** Expands method references into their corresponding argument/receiver roles. @author Snehanjan Chatterjee */
public final class MethodReferenceLab {
    /** Invokes static, bound, unbound, constructor and composed functions. */
    public static void main(final String[] args) {
        final Function<String, Integer> parse = Integer::parseInt;
        final String text = " java ";
        final Supplier<String> bound = text::trim;
        final Function<String, String> unbound = String::trim;
        final Supplier<List<String>> create = ArrayList<String>::new;
        final IntFunction<String[]> array = String[]::new;
        System.out.println(parse.apply("42"));
        System.out.println(bound.get());
        System.out.println(unbound.apply(" jvm "));
        System.out.println(create.get().size());
        System.out.println(array.apply(2).length);
        final Function<String, String> trim = String::trim;
        final Function<String, Integer> length = String::length;
        final Predicate<String> nonempty = value -> !value.isEmpty();
        final Predicate<String> shortText = value -> value.length() <= 4;
        System.out.println(trim.andThen(length).apply(" java "));
        System.out.println(nonempty.and(shortText).test("java"));
    }
}
