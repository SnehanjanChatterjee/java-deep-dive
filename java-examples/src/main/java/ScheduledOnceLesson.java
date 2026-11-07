import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/** Schedules one result-bearing task without claiming an execution deadline. @author Snehanjan Chatterjee */
public final class ScheduledOnceLesson {
    /** Retrieves the task's actual completion rather than assuming the delay ran it. */
    public static void main(final String[] args) throws InterruptedException, ExecutionException {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            final ScheduledFuture<Integer> result = scheduler.schedule(() -> 42, 0, TimeUnit.MILLISECONDS);
            System.out.println(result.get());
        } finally { scheduler.shutdown(); }
    }
}
