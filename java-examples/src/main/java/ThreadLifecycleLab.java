import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Demonstrates cooperative interruption with observable lifecycle boundaries. @author Snehanjan Chatterjee */
public final class ThreadLifecycleLab {
    public static final class Worker implements Runnable {
        private final CountDownLatch started = new CountDownLatch(1);
        private final CountDownLatch gate = new CountDownLatch(1);
        private boolean interruptedAtExit;
        /** Signals entry and waits interruptibly without polling. */
        @Override public void run() {
            started.countDown();
            try { gate.await(); }
            catch (final InterruptedException cancelled) { Thread.currentThread().interrupt(); }
            interruptedAtExit = Thread.currentThread().isInterrupted();
        }
        /** Waits for a bounded startup signal. */
        public boolean awaitStarted() throws InterruptedException { return started.await(5, TimeUnit.SECONDS); }
        /** Returns an exit observation; callers must first establish worker completion. */
        public boolean interruptedAtExit() { return interruptedAtExit; }
    }
    /** Runs the cancellation protocol and joins the owned worker. */
    public static void main(final String[] args) throws InterruptedException {
        final Worker task = new Worker();
        final Thread worker = new Thread(task, "study-worker");
        System.out.println(worker.getState());
        worker.start();
        try {
            if (!task.awaitStarted()) { throw new IllegalStateException("worker startup timed out"); }
        } finally { worker.interrupt(); worker.join(); }
        System.out.println(worker.getState());
        System.out.println(task.interruptedAtExit());
    }
}
