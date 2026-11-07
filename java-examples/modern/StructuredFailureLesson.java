import java.util.concurrent.CountDownLatch;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;

/** Demonstrates sibling interruption and joined cleanup; Java 25 preview. @author Snehanjan Chatterjee */
public final class StructuredFailureLesson {
    /** Fails one child only after its sibling begins interruptible work. */
    public static void main(final String[] args) throws Exception {
        final CountDownLatch started = new CountDownLatch(1);
        final CountDownLatch cleaned = new CountDownLatch(1);
        try (final var scope = StructuredTaskScope.open()) {
            scope.fork(() -> {
                started.countDown();
                try { new CountDownLatch(1).await(); return 1; }
                finally { cleaned.countDown(); }
            });
            scope.fork(() -> {
                if (!started.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("sibling did not start"); }
                throw new IllegalArgumentException("child failed");
            });
            scope.join();
        } catch (final StructuredTaskScope.FailedException failure) {
            System.out.println(failure.getCause().getMessage());
        }
        System.out.println("cleanup remaining=" + cleaned.getCount());
    }
}
