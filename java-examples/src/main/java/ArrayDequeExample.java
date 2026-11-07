import java.util.ArrayDeque;
import java.util.Deque;

/** Demonstrates ArrayDeque contracts. @author Snehanjan Chatterjee */
public final class ArrayDequeExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final Deque<String> queue = new ArrayDeque<>();
        queue.offerLast("A"); queue.offerLast("B");
        System.out.println(queue.pollFirst());
        queue.offerLast("C");
        System.out.println(queue);
        queue.push("top");
        System.out.println(queue.pop());
        queue.clear();
        System.out.println(queue.pollFirst());
    }
}
