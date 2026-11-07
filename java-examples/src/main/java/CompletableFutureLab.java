import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Demonstrates dependent stages, independent combination and deliberate recovery. @author Snehanjan Chatterjee */
public final class CompletableFutureLab {
    /** Composes results without blocking a worker on work queued to its own pool. */
    public static void main(final String[] args) throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            final CompletableFuture<Integer> userId = CompletableFuture.supplyAsync(() -> 42, pool);
            final CompletableFuture<Integer> score = userId.thenCompose(id ->
                    CompletableFuture.supplyAsync(() -> id + 1, pool));
            final CompletableFuture<String> label = CompletableFuture.supplyAsync(() -> "score", pool);
            System.out.println(score.thenCombine(label, (value, name) -> name + "=" + value).get());
            final CompletableFuture<String> failed = new CompletableFuture<>();
            failed.completeExceptionally(new IllegalArgumentException("invalid input"));
            System.out.println(failed.handle((value, failure) -> failure == null ? value : "recovered").get());
            System.out.println(failed.isCompletedExceptionally());
        } finally {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) { pool.shutdownNow(); }
            } catch (final InterruptedException interrupted) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
                throw interrupted;
            }
        }
    }
}
