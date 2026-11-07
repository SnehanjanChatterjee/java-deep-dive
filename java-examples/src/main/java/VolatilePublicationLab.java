import java.util.Objects;

/** Publishes a complete immutable snapshot through one volatile reference. @author Snehanjan Chatterjee */
public final class VolatilePublicationLab {
    public static final class Snapshot {
        private final int base;
        private final int fee;
        /** Rejects invalid amounts before constructing a publishable value. */
        public Snapshot(final int base, final int fee) {
            if (base < 0 || fee < 0) { throw new IllegalArgumentException("nonnegative amounts required"); }
            Math.addExact(base, fee);
            this.base = base; this.fee = fee;
        }
        /** Reads the invariant-preserving pair without consulting shared mutable state. */
        public int total() { return base + fee; }
    }
    private volatile Snapshot current;
    /** Publishes only fully constructed non-null values. */
    public void publish(final Snapshot snapshot) { current = Objects.requireNonNull(snapshot, "snapshot"); }
    /** Returns one observed snapshot; null means none has been published yet. */
    public Snapshot read() { return current; }
    /** Demonstrates publication without using join as the reader's publication edge. */
    public static void main(final String[] args) throws InterruptedException {
        final VolatilePublicationLab store = new VolatilePublicationLab();
        final Thread writer = new Thread(() -> store.publish(new Snapshot(40, 2)));
        writer.start();
        try {
            final long deadline = System.nanoTime() + 5_000_000_000L;
            Snapshot observed;
            while ((observed = store.read()) == null) {
                if (System.nanoTime() - deadline >= 0) { throw new IllegalStateException("publication timed out"); }
                Thread.yield();
            }
            System.out.println(observed.total());
        } finally { writer.join(); }
    }
}
