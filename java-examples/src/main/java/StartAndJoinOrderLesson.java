/** Starts a separate worker execution and waits for its termination. */
public final class StartAndJoinOrderLesson {
    /** Prints the owner's continuation only after the worker's action has ended. */
    public static void main(final String[] args) throws InterruptedException {
        final Runnable task = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        final Thread worker = new Thread(task, "worker");
        worker.start();
        worker.join();
        System.out.println("after join");
        System.out.println(worker.getState());
    }
}
