/** Demonstrates interruption before or during an interruptible wait. @author Snehanjan Chatterjee */
public final class CooperativeStopLesson {
    public static final class Worker implements Runnable {
        private boolean cleaned;
        /** Stops after interruption and runs its cleanup on either exit path. */
        @Override public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) { Thread.sleep(1000); }
            } catch (final InterruptedException cancelled) {
                Thread.currentThread().interrupt();
            } finally {
                cleaned = true;
            }
        }
        /** Returns the cleanup observation; the caller must first join the worker. */
        public boolean cleaned() { return cleaned; }
    }
    /** Owns a worker through start, cancellation request and completed termination. */
    public static void main(final String[] args) throws InterruptedException {
        final Worker task = new Worker();
        final Thread worker = new Thread(task, "interruptible-worker");
        worker.start();
        worker.interrupt();
        worker.join();
        System.out.println("terminated=" + !worker.isAlive());
        System.out.println("cleanup=" + task.cleaned());
    }
}
