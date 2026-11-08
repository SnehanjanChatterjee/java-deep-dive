import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.StructuredTaskScope;

/** Uses the Java 25 preview first-success policy for two interchangeable candidates. @author Snehanjan Chatterjee */
public final class StructuredFirstSuccessLesson {
    /** Returns one successful candidate while scope closure accounts for the other child. */
    public static String select(final Callable<String> first, final Callable<String> second)
        throws InterruptedException {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        try (var scope = StructuredTaskScope.open(
            StructuredTaskScope.Joiner.<String>anySuccessfulResultOrThrow())) {
            scope.fork(first);
            scope.fork(second);
            return scope.join();
        }
    }

    /** A failed candidate does not prevent the other candidate supplying the successful value. */
    public static void main(final String[] args) throws InterruptedException {
        final CountDownLatch failurePath = new CountDownLatch(1);
        System.out.println(select(() -> {
            try {
                throw new IllegalArgumentException("candidate unavailable");
            } finally {
                failurePath.countDown();
            }
        }, () -> {
            failurePath.await();
            return "fallback";
        }));
    }
}
