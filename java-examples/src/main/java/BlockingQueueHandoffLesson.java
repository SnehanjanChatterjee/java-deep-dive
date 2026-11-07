import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** Connects one producer to one finite-batch consumer through a library queue. @author Snehanjan Chatterjee */
public final class BlockingQueueHandoffLesson {
    private static final class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private int total;
        private InterruptedException failure;
        /** Accepts the queue whose three-item batch this consumer owns. */
        private Consumer(final BlockingQueue<Integer> queue) { this.queue = queue; }
        /** Receives the declared number of items, preserving producer FIFO order. */
        @Override public void run() {
            try {
                for (int index = 0; index < 3; index++) {
                    final int value = queue.take();
                    System.out.println("consumed=" + value);
                    total += value;
                }
            } catch (final InterruptedException cancelled) {
                failure = cancelled; Thread.currentThread().interrupt();
            }
        }
    }
    /** Supplies exactly one declared batch and joins its consumer on every owner path. */
    public static void main(final String[] args) throws InterruptedException {
        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);
        final Consumer task = new Consumer(queue);
        final Thread consumer = new Thread(task, "batch-consumer");
        consumer.start();
        try {
            queue.put(10); queue.put(20); queue.put(12);
            consumer.join();
            if (task.failure != null) { throw new IllegalStateException("batch interrupted", task.failure); }
            System.out.println("total=" + task.total);
        } finally {
            consumer.interrupt(); consumer.join();
        }
    }
}
