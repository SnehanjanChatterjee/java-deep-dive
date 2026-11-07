import java.util.concurrent.atomic.AtomicInteger;

/** Compares two correct increment protocols after owned workers finish. @author Snehanjan Chatterjee */
public final class CounterComparisonLab {
    private static final class LockedCounter {
        private int value;
        /** Makes the entire increment indivisible under this receiver's monitor. */
        private synchronized void increment() { value++; }
        /** Reads using the same monitor protocol. */
        private synchronized int value() { return value; }
    }
    public static final class Result {
        private final int synchronizedCount;
        private final int atomicCount;
        /** Stores already completed observations. */
        private Result(final int synchronizedCount, final int atomicCount) { this.synchronizedCount = synchronizedCount; this.atomicCount = atomicCount; }
        /** Returns the synchronized implementation's completed result. */
        public int synchronizedCount() { return synchronizedCount; }
        /** Returns the atomic implementation's completed result. */
        public int atomicCount() { return atomicCount; }
    }
    /** Runs two finite workers and publishes completed observations through join. */
    public static Result count(final int iterationsPerWorker) throws InterruptedException {
        if (iterationsPerWorker < 0) { throw new IllegalArgumentException("negative iteration count"); }
        Math.multiplyExact(iterationsPerWorker, 2);
        final LockedCounter locked = new LockedCounter();
        final AtomicInteger atomic = new AtomicInteger();
        final Runnable increment = () -> {
            for (int i = 0; i < iterationsPerWorker; i++) { locked.increment(); atomic.incrementAndGet(); }
        };
        final Thread first = new Thread(increment); final Thread second = new Thread(increment);
        first.start(); second.start(); first.join(); second.join();
        return new Result(locked.value(), atomic.get());
    }
    /** Compares correctness outcomes, without claiming a performance measurement. */
    public static void main(final String[] args) throws InterruptedException {
        final Result result = count(1000);
        System.out.println(result.synchronizedCount()); System.out.println(result.atomicCount());
    }
}
