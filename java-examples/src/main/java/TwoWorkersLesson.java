/** Shares one task description between two separate finite executions. @author Snehanjan Chatterjee */
public final class TwoWorkersLesson {
    /** Starts both workers before joining either and prints after both terminate. */
    public static void main(final String[] args) throws InterruptedException {
        final Runnable task = () -> System.out.println("task completed");
        final Thread first = new Thread(task, "first-worker");
        final Thread second = new Thread(task, "second-worker");
        first.start(); second.start();
        first.join(); second.join();
        System.out.println("all finished");
    }
}
