import java.util.ListIterator;
import java.util.Arrays;
import java.util.LinkedList;

/** Demonstrates LinkedList contracts. @author Snehanjan Chatterjee */
public final class LinkedListExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final LinkedList<String> words = new LinkedList<>(Arrays.asList("A", "C"));
        final ListIterator<String> cursor = words.listIterator(1);
        cursor.add("B");
        System.out.println(words);
        System.out.println(cursor.next());
        cursor.remove();
        System.out.println(words);
        words.addFirst("start");
        System.out.println(words.removeLast());
    }
}
