
/** Select diagnostic evidence that matches the suspected failure. @author Snehanjan Chatterjee */
public final class ThreadInspection {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final Thread worker = new Thread(() -> { });
        worker.start(); worker.join();
        System.out.println(worker.getState());
    }
}
