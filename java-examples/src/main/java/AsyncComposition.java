import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Define capacity, cancellation and failure behavior for asynchronous work. @author Snehanjan Chatterjee */
public final class AsyncComposition {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            final Future<Integer> value = pool.submit(() -> 21 * 2);
            System.out.println(value.get());
            System.out.println(CompletableFuture.completedFuture(10).thenApply(n -> n + 5).join());
        } finally { pool.shutdownNow(); }
    }
}
