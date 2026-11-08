import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** Uses a single worker across tasks and closes the owned service lifetime. @author Snehanjan Chatterjee */
public final class ExecutorFactoryLesson {
    /** Computes dependent results without blocking the sole worker on queued child work. */
    public static int runBatch() throws InterruptedException, ExecutionException {
        final ExecutorService service = Executors.newSingleThreadExecutor(
            task -> new Thread(task, "batch-worker"));
        try {
            final Future<Integer> first = service.submit(() -> 21);
            final int input = first.get();
            final Future<Integer> second = service.submit(() -> input * 2);
            return second.get();
        } finally {
            service.shutdown();
            try {
                if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                    if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("batch worker did not terminate");
                    }
                }
            } catch (final InterruptedException interrupted) {
                service.shutdownNow();
                throw interrupted;
            }
        }
    }

    /** Main owns the service indirectly through the complete finite batch. */
    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        System.out.println(runBatch());
    }
}
