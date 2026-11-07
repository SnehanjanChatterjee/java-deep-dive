import java.util.concurrent.atomic.AtomicInteger;

/** Separate visibility, ordering and atomicity when reasoning about shared state. @author Snehanjan Chatterjee */
public final class MemoryVisibility {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final AtomicInteger count = new AtomicInteger();
        final Thread worker = new Thread(count::incrementAndGet);
        worker.start(); worker.join();
        System.out.println(count.get());
    }
}
