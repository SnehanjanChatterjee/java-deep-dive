import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/** Contrasts a rejected immediate offer with a paired zero-buffer transfer. @author Snehanjan Chatterjee */
public final class SynchronousHandoffLesson {
    /** Transfers one item to one known consumer and joins that consumer before returning. */
    public static String[] observations() throws InterruptedException {
        final SynchronousQueue<String> handoff = new SynchronousQueue<>();
        final boolean acceptedWithoutConsumer = handoff.offer("unpaired");
        final String[] received = new String[1];
        final InterruptedException[] failure = new InterruptedException[1];
        final Thread consumer = new Thread(() -> {
            try {
                received[0] = handoff.take();
            } catch (final InterruptedException interrupted) {
                failure[0] = interrupted;
                Thread.currentThread().interrupt();
            }
        }, "handoff-consumer");
        consumer.start();
        try {
            if (!handoff.offer("payload", 5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("consumer did not receive the handoff");
            }
            consumer.join();
            if (failure[0] != null) {
                throw new IllegalStateException("consumer was interrupted", failure[0]);
            }
            return new String[] {"unpaired accepted=" + acceptedWithoutConsumer,
                "received=" + received[0], "stored=" + handoff.size()};
        } finally {
            consumer.interrupt();
            consumer.join();
        }
    }

    /** Prints transfer outcomes rather than a guessed producer/consumer schedule. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
