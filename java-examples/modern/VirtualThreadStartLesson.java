/** Starts and joins one virtual thread using the stable Java 21 API. @author Snehanjan Chatterjee */
public final class VirtualThreadStartLesson {
    /** Uses the familiar Thread lifetime with a different scheduler. */
    public static void main(final String[] args) throws InterruptedException {
        final Thread worker = Thread.ofVirtual().name("virtual-worker").start(
                () -> System.out.println("virtual=" + Thread.currentThread().isVirtual()));
        worker.join();
        System.out.println("finished=" + !worker.isAlive());
    }
}
