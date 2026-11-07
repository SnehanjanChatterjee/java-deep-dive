import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Separates branch recovery, observation failure and all-of completion. @author Snehanjan Chatterjee */
public final class CompletionOutcomeLesson {
    /** Completes controlled stages without relying on scheduling or network timing. */
    public static void main(final String[] args) {
        final CompletableFuture<String> source = new CompletableFuture<>();
        final CompletableFuture<String> recovery = source.handle((value, failure) -> failure == null ? value : "fallback");
        final CompletableFuture<String> observation = source.whenComplete((value, failure) -> {
            throw new IllegalArgumentException("observer");
        });
        final CompletableFuture<String> other = new CompletableFuture<>();
        final CompletableFuture<Void> all = CompletableFuture.allOf(source, other);
        source.completeExceptionally(new IllegalStateException("source"));
        System.out.println(recovery.join());
        System.out.println("source failed=" + source.isCompletedExceptionally());
        try { observation.join(); }
        catch (final CompletionException failure) { System.out.println("observed cause=" + failure.getCause().getMessage()); }
        System.out.println("all still waiting=" + !all.isDone());
        other.complete("done");
        try { all.join(); }
        catch (final CompletionException failure) { System.out.println("all cause=" + failure.getCause().getMessage()); }
    }
}
