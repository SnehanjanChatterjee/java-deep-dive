import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Restores nested context on a reused worker instead of leaking task state. @author Snehanjan Chatterjee */
public final class ThreadContextLesson {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();
    /** Binds one value for an action and restores the previous binding in finally. */
    public static void withContext(final String value, final Runnable action) {
        Objects.requireNonNull(value, "value"); Objects.requireNonNull(action, "action");
        final String previous = CONTEXT.get();
        CONTEXT.set(value);
        try { action.run(); }
        finally { if (previous == null) { CONTEXT.remove(); } else { CONTEXT.set(previous); } }
    }
    /** Returns the current binding, or null when absent. */
    public static String current() { return CONTEXT.get(); }
    /** Shows nested restoration and cleanup before another task reuses the worker. */
    public static void main(final String[] args) throws Exception {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.submit(() -> withContext("outer", () -> {
                System.out.println(current());
                withContext("inner", () -> System.out.println(current()));
                System.out.println(current());
            })).get();
            System.out.println(executor.submit(ThreadContextLesson::current).get());
        } finally { executor.shutdownNow(); }
    }
}
