import java.util.concurrent.ConcurrentLinkedQueue;

/** Uses one nonblocking FIFO without treating emptiness as permanent closure. @author Snehanjan Chatterjee */
public final class ConcurrentQueueDrainLesson {
    /** Reads ordinary empty and nonempty operation outcomes. */
    public static void main(final String[] args) {
        final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        System.out.println(queue.poll());
        queue.offer("A"); queue.offer("B");
        System.out.println(queue.peek());
        System.out.println(queue.poll());
        queue.offer("C");
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
