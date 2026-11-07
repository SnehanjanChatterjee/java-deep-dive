/** Calls a Thread object's run method without starting a new execution. */
public final class RunDoesNotStartLesson {
    /** Runs the action on main and then observes the still-unstarted Thread object. */
    public static void main(final String[] args) {
        final Runnable task = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        final Thread worker = new Thread(task, "worker");
        worker.run();
        System.out.println("after run");
        System.out.println(worker.getState());
    }
}
