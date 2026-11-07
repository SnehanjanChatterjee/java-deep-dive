import java.util.concurrent.CompletableFuture;

/** Isolates value transformation, future flattening and independent combination. @author Snehanjan Chatterjee */
public final class CompletionStepsLesson {
    /** Represents a result-bearing dependent operation without adding scheduling. */
    private static CompletableFuture<Integer> score(final int userId) {
        return CompletableFuture.completedFuture(userId + 1);
    }
    /** Uses already completed values so dependency shapes can be followed independently. */
    public static void main(final String[] args) {
        final CompletableFuture<Integer> doubled = CompletableFuture.completedFuture(21).thenApply(value -> value * 2);
        System.out.println(doubled.join());
        final CompletableFuture<Integer> user = CompletableFuture.completedFuture(42);
        final CompletableFuture<CompletableFuture<Integer>> nested = user.thenApply(CompletionStepsLesson::score);
        final CompletableFuture<Integer> flattened = user.thenCompose(CompletionStepsLesson::score);
        System.out.println(nested.join().join());
        System.out.println(flattened.join());
        final CompletableFuture<String> label = CompletableFuture.completedFuture("score=");
        System.out.println(label.thenCombine(flattened, (text, value) -> text + value).join());
    }
}
