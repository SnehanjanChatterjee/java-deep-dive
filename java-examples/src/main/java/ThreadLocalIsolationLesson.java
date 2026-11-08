/** Uses one key with independent main-thread and worker-thread associations. @author Snehanjan Chatterjee */
public final class ThreadLocalIsolationLesson {
    /** Removes each owned association at that thread's boundary and returns joined observations. */
    public static int[] observations() throws InterruptedException {
        final ThreadLocal<Integer> local = ThreadLocal.withInitial(() -> 0);
        final int[] values = new int[3];
        local.set(7);
        final Thread worker = new Thread(() -> {
            try {
                values[0] = local.get();
                local.set(1);
                values[1] = local.get();
            } finally {
                local.remove();
            }
        }, "isolated-context-worker");
        try {
            worker.start();
            worker.join();
            values[2] = local.get();
            return values;
        } finally {
            local.remove();
        }
    }

    /** The worker starts at its initializer's zero rather than inheriting main's seven. */
    public static void main(final String[] args) throws InterruptedException {
        for (final int value : observations()) {
            System.out.println(value);
        }
    }
}
