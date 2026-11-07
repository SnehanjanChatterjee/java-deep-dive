import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/** Executes independent tasks on virtual threads. @author Snehanjan Chatterjee */
public final class VirtualThreadsDemo {
    /** Waits for both results and closes the executor's task lifetime. */
    public static void main(final String[] args) throws Exception {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final Future<Boolean> virtual = executor.submit(() -> Thread.currentThread().isVirtual());
            final Future<Integer> result = executor.submit(() -> 21 * 2);
            System.out.println(virtual.get());
            System.out.println(result.get());
        }
    }
}
