import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/** Demonstrates PriorityQueue and binary heaps contracts. @author Snehanjan Chatterjee */
public final class PriorityQueueExample {
    /** Runs a deterministic API and edge-case trace. */
    public static void main(final String[] args) {
        final PriorityQueue<Integer> minimums = new PriorityQueue<>();
        minimums.addAll(Arrays.asList(4, 1, 3, 2));
        System.out.println(minimums.peek());
        while (!minimums.isEmpty()) { System.out.println(minimums.poll()); }
        final PriorityQueue<Integer> maximums = new PriorityQueue<>(Comparator.reverseOrder());
        maximums.addAll(Arrays.asList(4, 1, 3, 2));
        System.out.println(maximums.poll());
    }
}
