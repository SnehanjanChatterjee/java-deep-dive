import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/** Composes predicates and transformations while preserving their evaluation order. @author Snehanjan Chatterjee */
public final class PredicateFunctions {
    /** Selects values in source order; the caller's predicate owns its element/null policy. */
    public static <T> List<T> select(final List<T> source, final Predicate<? super T> predicate) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(predicate, "predicate");
        final List<T> selected = new ArrayList<>();
        for (final T value : source) { if (predicate.test(value)) { selected.add(value); } }
        return selected;
    }
    /** Uses a short-circuit null guard and records the second predicate's invocation count. */
    public static int guardedLengthChecks(final List<String> values) {
        Objects.requireNonNull(values, "values");
        final int[] calls = {0};
        final Predicate<String> nonnull = value -> value != null;
        final Predicate<String> length = text -> { calls[0]++; return text.length() >= 3; };
        select(values, nonnull.and(length));
        return calls[0];
    }
    /** Contrasts function order, consumer sequencing and independently supplied containers. */
    public static void main(final String[] args) {
        final Predicate<String> named = Predicate.isEqual("Java");
        System.out.println(named.test(new String("Java")));
        System.out.println(named.negate().test("JVM"));
        final List<String> input = Arrays.asList(null, "a", "Java");
        System.out.println(select(input, value -> value != null));
        System.out.println("length calls=" + guardedLengthChecks(input));
        final Predicate<String> absent = value -> value == null;
        final Predicate<String> shortText = text -> text.length() < 3;
        System.out.println("or skips unsafe branch=" + absent.or(shortText).test(null));
        final Function<Integer, Integer> plusOne = value -> value + 1;
        final Function<Integer, Integer> timesTwo = value -> value * 2;
        System.out.println("andThen/compose=" + plusOne.andThen(timesTwo).apply(3) + "/" + plusOne.compose(timesTwo).apply(3));
        final StringBuilder order = new StringBuilder();
        final Consumer<String> first = text -> order.append('A');
        final Consumer<String> second = text -> order.append('B');
        first.andThen(second).accept("ignored");
        System.out.println(order);
        final Supplier<List<String>> fresh = () -> new ArrayList<>();
        System.out.println("fresh containers=" + (fresh.get() != fresh.get()));
        final List<String> editable = new ArrayList<>(input);
        editable.removeIf(text -> text == null || text.length() < 3);
        editable.replaceAll(text -> text.toUpperCase(Locale.ROOT));
        System.out.println("removeIf/replaceAll=" + editable);
    }
}
