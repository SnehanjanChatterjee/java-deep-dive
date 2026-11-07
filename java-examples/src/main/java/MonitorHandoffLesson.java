/** Coordinates a real waiting consumer using the same guarded-state protocol. @author Snehanjan Chatterjee */
public final class MonitorHandoffLesson {
    private static final class Mailbox {
        private String value;
        private boolean consumerWaiting;
        /** Announces the waiting milestone and waits for the durable value predicate. */
        private synchronized String take() throws InterruptedException {
            consumerWaiting = true;
            notifyAll();
            System.out.println("consumer waits");
            while (value == null) { wait(); }
            System.out.println("consumer receives " + value);
            return value;
        }
        /** Waits for the announced milestone without guessing elapsed time. */
        private synchronized void awaitConsumer() throws InterruptedException {
            while (!consumerWaiting) { wait(); }
        }
        /** Changes the predicate before notifying and before releasing ownership. */
        private synchronized void put(final String message) {
            value = message;
            System.out.println("producer stores " + message);
            notifyAll();
        }
    }
    private static final class Receiver implements Runnable {
        private final Mailbox mailbox;
        private String result;
        private InterruptedException failure;
        /** Retains the shared mailbox. */
        private Receiver(final Mailbox mailbox) { this.mailbox = mailbox; }
        /** Records an outcome that the owner reads after join. */
        @Override public void run() {
            try { result = mailbox.take(); }
            catch (final InterruptedException cancelled) { failure = cancelled; Thread.currentThread().interrupt(); }
        }
    }
    /** Performs one handoff; only the owner interrupts the finite demonstration worker. */
    public static String exchange() throws InterruptedException {
        final Mailbox mailbox = new Mailbox();
        final Receiver receiver = new Receiver(mailbox);
        final Thread consumer = new Thread(receiver, "mailbox-consumer");
        consumer.start();
        try {
            mailbox.awaitConsumer();
            mailbox.put("message");
            consumer.join();
            if (receiver.failure != null) { throw new IllegalStateException("consumer interrupted", receiver.failure); }
            return receiver.result;
        } finally {
            consumer.interrupt();
            consumer.join();
        }
    }
    /** Runs the coordinated wait, store and reacquisition trace. */
    public static void main(final String[] args) throws InterruptedException { exchange(); }
}
