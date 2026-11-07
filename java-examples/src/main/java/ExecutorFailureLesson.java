import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Observes a submitted failure without pretending it is an ordinary result. @author Snehanjan Chatterjee */
public final class ExecutorFailureLesson {
    /** Retrieves the failed task, then proves a later task can still use the service. */
    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Future<Integer> failed = executor.submit(() -> { throw new IllegalArgumentException("invalid score"); });
            try {
                failed.get();
                throw new IllegalStateException("failure was not reported");
            } catch (final ExecutionException expected) {
                if (!(expected.getCause() instanceof IllegalArgumentException)) { throw expected; }
                System.out.println(expected.getCause().getMessage());
            }
            System.out.println(executor.submit(() -> 42).get());
        } finally {
            executor.shutdown();
        }
    }
}
