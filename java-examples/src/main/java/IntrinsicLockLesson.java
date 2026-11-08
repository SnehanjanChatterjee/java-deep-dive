/** Identifies the actual object selected by each synchronized form. @author Snehanjan Chatterjee */
public final class IntrinsicLockLesson {
    private final Object guard = new Object();

    /** An instance synchronized method owns this receiver's monitor. */
    public synchronized boolean holdsReceiver() {
        return Thread.holdsLock(this);
    }

    /** A private guard is a different monitor from this receiver. */
    public boolean holdsPrivateGuardOnly() {
        synchronized (guard) {
            return Thread.holdsLock(guard) && !Thread.holdsLock(this);
        }
    }

    /** A static synchronized method owns the declaring class object's monitor. */
    public static synchronized boolean holdsClassMonitor() {
        return Thread.holdsLock(IntrinsicLockLesson.class);
    }

    /** Traces monitor identity without starting an indefinitely blocked experiment. */
    public static void main(final String[] args) {
        final IntrinsicLockLesson first = new IntrinsicLockLesson();
        final IntrinsicLockLesson second = new IntrinsicLockLesson();
        System.out.println("instance method=" + first.holdsReceiver());
        synchronized (first) {
            System.out.println("other instance locked=" + Thread.holdsLock(second));
            System.out.println("class locked=" + Thread.holdsLock(IntrinsicLockLesson.class));
        }
        System.out.println("private guard only=" + first.holdsPrivateGuardOnly());
        System.out.println("static method=" + holdsClassMonitor());
    }
}
