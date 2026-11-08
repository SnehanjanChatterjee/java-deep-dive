import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** Retrieves finished tasks in completion order rather than submission order. @author Snehanjan Chatterjee */
public final class CompletionOrderLesson {
    /** Holds the first submission until the second result has been retrieved. */
    public static int[] collect() throws InterruptedException, ExecutionException {
        final ExecutorService workers = Executors.newFixedThreadPool(2);
        final ExecutorCompletionService<Integer> completed = new ExecutorCompletionService<>(workers);
        final CountDownLatch releaseFirst = new CountDownLatch(1);
        final Future<Integer> first = completed.submit(() -> {
            releaseFirst.await();
            return 1;
        });
        final Future<Integer> second = completed.submit(() -> 42);
        try {
            final Future<Integer> ready = completed.poll(5, TimeUnit.SECONDS);
            if (ready == null) {
                throw new IllegalStateException("second task did not finish");
            }
            final int firstRetrieved = ready.get();
            releaseFirst.countDown();
            final Future<Integer> remaining = completed.poll(5, TimeUnit.SECONDS);
            if (remaining == null) {
                throw new IllegalStateException("released task did not finish");
            }
            return new int[] {firstRetrieved, remaining.get()};
        } finally {
            releaseFirst.countDown();
            first.cancel(true);
            second.cancel(true);
            workers.shutdownNow();
            if (!workers.awaitTermination(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("completion workers did not terminate");
            }
        }
    }

    /** The gate establishes the printed order; no relative-speed assumption is needed. */
    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        final int[] answers = collect();
        System.out.println("first completed=" + answers[0]);
        System.out.println("then completed=" + answers[1]);
    }
}
