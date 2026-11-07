import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Demonstrates ArrayList contracts. @author Snehanjan Chatterjee */
public final class ArrayListExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final List<Integer> values = new ArrayList<>(Arrays.asList(10, 20, 30));
        values.add(1, 15);
        System.out.println(values);
        System.out.println(values.remove(1));
        values.remove(Integer.valueOf(20));
        System.out.println(values);
        final List<Integer> view = values.subList(0, 1);
        view.set(0, 99);
        System.out.println(values);
    }
}
