import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

/** Choose collections by ordering, uniqueness, access and mutation requirements. @author Snehanjan Chatterjee */
public final class CollectionChoices {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Set<String> unique = new LinkedHashSet<>(Arrays.asList("b", "a", "b"));
        System.out.println(unique);
        final Deque<String> queue = new ArrayDeque<>();
        queue.offer("first"); queue.offer("second");
        System.out.println(queue.poll());
    }
}
