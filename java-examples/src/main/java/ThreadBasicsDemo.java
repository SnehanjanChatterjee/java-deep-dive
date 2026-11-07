/** Separates a task, an ordinary call and a new thread of execution. @author Snehanjan Chatterjee */
public final class ThreadBasicsDemo {
    /** Runs the same task first on main, then on an owned worker. */
    public static void main(final String[] args) throws InterruptedException {
        final Runnable task = () -> System.out.println("task on " + Thread.currentThread().getName());
        task.run();
        final Thread worker = new Thread(task, "lesson-worker");
        worker.start();
        worker.join();
        System.out.println("worker finished=" + !worker.isAlive());
    }
}
