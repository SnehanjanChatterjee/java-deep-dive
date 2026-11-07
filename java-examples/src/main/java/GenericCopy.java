import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Design reusable APIs with sound producer and consumer types. @author Snehanjan Chatterjee */
public final class GenericCopy {
    static <T> void copy(final List<? extends T> source, final List<? super T> target) {
        for (final T value : source) { target.add(value); }
    }
    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final List<Integer> source = Arrays.asList(1, 2);
        final List<Number> target = new ArrayList<>();
        copy(source, target);
        System.out.println(target);
    }
}
