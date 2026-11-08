import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Contrasts empty completion aggregates and an invalid composition result. @author Snehanjan Chatterjee */
public final class CompletionEdgeLesson {
    /** Inspects a pending empty anyOf without accidentally waiting on it forever. */
    public static String[] observations() {
        final CompletableFuture<Void> all = CompletableFuture.allOf();
        final CompletableFuture<Object> any = CompletableFuture.anyOf();
        final CompletableFuture<Integer> invalid = CompletableFuture.completedFuture(21)
            .thenCompose(value -> null);
        String failure = "missing failure";
        try {
            invalid.join();
        } catch (final CompletionException expected) {
            failure = "null composition=" + expected.getCause().getClass().getSimpleName();
        }
        return new String[] {"empty all done=" + all.isDone(), "empty all value=" + all.join(),
            "empty any done=" + any.isDone(), failure};
    }

    /** Empty allOf is already successful; empty anyOf has no completion to select. */
    public static void main(final String[] args) {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
