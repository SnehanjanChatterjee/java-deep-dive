/** Separates names, object identity and daemon configuration. @author Snehanjan Chatterjee */
public final class ThreadMetadataLesson {
    /** Reports stable properties without guessing scheduler decisions or numeric IDs. */
    public static String[] observations() throws InterruptedException {
        final Thread first = new Thread(() -> { }, "same-name");
        final Thread second = new Thread(() -> { }, "same-name");
        final boolean[] ranAsItself = new boolean[1];
        final Thread daemon = new Thread(() -> {
            ranAsItself[0] = Thread.currentThread().isDaemon();
        }, "finite-daemon");
        daemon.setDaemon(true);
        daemon.setPriority(Thread.NORM_PRIORITY);
        daemon.start();
        daemon.join();
        return new String[] {
            "same name=" + first.getName().equals(second.getName()),
            "same object=" + (first == second),
            "caller is first=" + (Thread.currentThread() == first),
            "worker observed daemon=" + ranAsItself[0],
            "worker finished=" + !daemon.isAlive()
        };
    }

    /** Joins the daemon explicitly so required work cannot be abandoned at main's return. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
