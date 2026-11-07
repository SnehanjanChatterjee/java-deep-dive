import java.util.concurrent.locks.StampedLock;

/** Shows validated optimistic reads of a two-field invariant. @author Snehanjan Chatterjee */
public final class OptimisticPoint {
    private final StampedLock lock = new StampedLock();
    private double x;
    private double y;

    /** Changes both coordinates under an exclusive stamp. */
    public void move(final double deltaX, final double deltaY) {
        final long stamp = lock.writeLock();
        try { x += deltaX; y += deltaY; }
        finally { lock.unlockWrite(stamp); }
    }

    /** Returns a distance from one validated pair of coordinates. */
    public double distance() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x;
        double currentY = y;
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try { currentX = x; currentY = y; }
            finally { lock.unlockRead(stamp); }
        }
        return Math.hypot(currentX, currentY);
    }

    /** Runs deterministic sequential examples; interleaving rules are explained in the lesson. */
    public static void main(final String[] args) {
        final OptimisticPoint point = new OptimisticPoint();
        point.move(3, 4); System.out.println(point.distance());
        point.move(3, 4); System.out.println(point.distance());
    }
}
