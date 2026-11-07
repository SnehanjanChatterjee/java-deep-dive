/** Publishes a worker's ordinary field through successful join. @author Snehanjan Chatterjee */
public final class JoinPublicationLesson {
    private int result;
    /** Waits for the writer before observing its result, without an atomic wrapper. */
    public static void main(final String[] args) throws InterruptedException {
        final JoinPublicationLesson calculation = new JoinPublicationLesson();
        final Thread worker = new Thread(() -> calculation.result = 42);
        worker.start();
        worker.join();
        System.out.println(calculation.result);
    }
}
