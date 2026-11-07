import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Demonstrates atomic per-key updates and explicit task completion. @author Snehanjan Chatterjee */
public final class ConcurrentMapExample {
    /** Runs two writers and observes their completed atomic updates. */
    public static void main(final String[] args) throws Exception {
        final ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        final ExecutorService workers = Executors.newFixedThreadPool(2);
        try {
            final Runnable increment = () -> {
                for (int i = 0; i < 1000; i++) { counts.merge("java", 1, Integer::sum); }
            };
            final Future<?> first = workers.submit(increment);
            final Future<?> second = workers.submit(increment);
            first.get(); second.get();
            System.out.println(counts.get("java"));
            System.out.println(counts.putIfAbsent("java", 9));
            System.out.println(counts.get("java"));
            try { counts.put("invalid", null); }
            catch (final NullPointerException expected) { System.out.println("null rejected"); }
        } finally { workers.shutdownNow(); }
    }
}
