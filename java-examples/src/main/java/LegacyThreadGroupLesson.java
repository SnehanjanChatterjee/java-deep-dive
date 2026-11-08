/** Shows group membership while retaining explicit ownership of each thread. @author Snehanjan Chatterjee */
public final class LegacyThreadGroupLesson {
    /** Returns recorded membership after both named workers have terminated. */
    public static String[] memberships() throws InterruptedException {
        final ThreadGroup group = new ThreadGroup("lesson-group");
        final String[] observed = new String[2];
        final Thread first = new Thread(group,
            () -> observed[0] = Thread.currentThread().getThreadGroup().getName(), "first");
        final Thread second = new Thread(group,
            () -> observed[1] = Thread.currentThread().getThreadGroup().getName(), "second");
        first.start();
        second.start();
        first.join();
        second.join();
        return observed;
    }

    /** Group construction creates no execution; start and join still belong to Threads. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String membership : memberships()) {
            System.out.println(membership);
        }
    }
}
