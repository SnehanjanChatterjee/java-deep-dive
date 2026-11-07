import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** Makes several blocked virtual tasks visible without a timing comparison. @author Snehanjan Chatterjee */
public final class VirtualBlockingLesson {
    /** Starts all tasks, observes their waiting milestone, then releases their work. */
    public static void main(final String[] args) throws Exception {
        final CountDownLatch entered = new CountDownLatch(3);
        final CountDownLatch release = new CountDownLatch(1);
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            try {
                final List<Future<Integer>> results = new ArrayList<>();
                for (int index = 0; index < 3; index++) {
                    results.add(executor.submit(() -> {
                        entered.countDown();
                        release.await();
                        return 42;
                    }));
                }
                if (!entered.await(5, TimeUnit.SECONDS)) { throw new IllegalStateException("tasks did not enter"); }
                System.out.println("entered tasks=3");
                release.countDown();
                int total = 0;
                for (final Future<Integer> result : results) { total += result.get(); }
                System.out.println("total=" + total);
            } finally { release.countDown(); }
        }
    }
}
