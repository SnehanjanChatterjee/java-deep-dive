import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Submits one result-bearing task and observes its completed value. @author Snehanjan Chatterjee */
public final class ExecutorBasicsLesson {
    /** Separates the task description, execution service and result handle. */
    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Callable<Integer> task = () -> 21 * 2;
            final Future<Integer> result = executor.submit(task);
            System.out.println(result.get());
        } finally {
            executor.shutdown();
        }
    }
}
