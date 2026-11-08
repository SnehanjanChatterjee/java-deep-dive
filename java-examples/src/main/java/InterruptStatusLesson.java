/** Observes flag inspection, consumption and an interruptible operation on an owned worker. @author Snehanjan Chatterjee */
public final class InterruptStatusLesson {
    /** Returns the worker's observations after joining; the caller's flag is untouched. */
    public static String[] observations() throws InterruptedException {
        final String[] answers = new String[6];
        final Thread worker = new Thread(() -> {
            final Thread self = Thread.currentThread();
            self.interrupt();
            answers[0] = "first inspect=" + self.isInterrupted();
            answers[1] = "second inspect=" + self.isInterrupted();
            answers[2] = "consume=" + Thread.interrupted();
            answers[3] = "after consume=" + self.isInterrupted();
            self.interrupt();
            try {
                Thread.sleep(1);
                throw new AssertionError("sleep must detect the pending interruption");
            } catch (final InterruptedException expected) {
                answers[4] = "sleep threw";
                answers[5] = "after sleep exception=" + self.isInterrupted();
            }
        }, "status-worker");
        worker.start();
        worker.join();
        return answers;
    }

    /** Prints each observation in the order made by the worker. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String answer : observations()) {
            System.out.println(answer);
        }
    }
}
