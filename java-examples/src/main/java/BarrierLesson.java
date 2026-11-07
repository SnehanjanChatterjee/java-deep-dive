import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/** Reuses one barrier for two rounds of independent slot updates. @author Snehanjan Chatterjee */
public final class BarrierLesson {
    private static final class Worker implements Runnable {
        private final int index;
        private final int[] slots;
        private final CyclicBarrier barrier;
        private Throwable failure;
        /** Assigns one result slot to this worker. */
        private Worker(final int index, final int[] slots, final CyclicBarrier barrier) {
            this.index = index; this.slots = slots; this.barrier = barrier;
        }
        /** Reaches the same barrier once in each logical phase. */
        @Override public void run() {
            try {
                for (int phase = 1; phase <= 2; phase++) {
                    slots[index] = (index + 1) * phase;
                    barrier.await();
                }
            } catch (final InterruptedException cancelled) {
                failure = cancelled; Thread.currentThread().interrupt();
            } catch (final BrokenBarrierException broken) {
                failure = broken;
            }
        }
    }
    /** Runs the barrier action before either participant enters the next generation. */
    public static void main(final String[] args) throws InterruptedException {
        final int[] slots = new int[2];
        final CyclicBarrier barrier = new CyclicBarrier(2,
                () -> System.out.println("phase total=" + (slots[0] + slots[1])));
        final Worker left = new Worker(0, slots, barrier);
        final Worker right = new Worker(1, slots, barrier);
        final Thread first = new Thread(left);
        final Thread second = new Thread(right);
        first.start(); second.start();
        try { first.join(); second.join(); }
        finally { first.interrupt(); second.interrupt(); first.join(); second.join(); }
        if (left.failure != null) { throw new IllegalStateException("left phase failed", left.failure); }
        if (right.failure != null) { throw new IllegalStateException("right phase failed", right.failure); }
    }
}
