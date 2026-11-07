import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/** Implements a bounded CAS reservation and demonstrates stamped ABA detection. @author Snehanjan Chatterjee */
public final class AtomicCounterLab {
    public static final class Budget {
        private final AtomicInteger remaining;
        /** Creates a nonnegative reservation budget. */
        public Budget(final int capacity) {
            if (capacity < 0) { throw new IllegalArgumentException("negative capacity"); }
            remaining = new AtomicInteger(capacity);
        }
        /** Atomically reserves positive units or returns false without changing the budget. */
        public boolean tryReserve(final int units) {
            if (units <= 0) { throw new IllegalArgumentException("positive units required"); }
            for (;;) {
                final int previous = remaining.get();
                if (units > previous) { return false; }
                if (remaining.compareAndSet(previous, previous - units)) { return true; }
            }
        }
        /** Observes the remaining units with the atomic variable's read semantics. */
        public int remaining() { return remaining.get(); }
    }
    /** Demonstrates successful/failed reservations and an intervening A-B-A history. */
    public static void main(final String[] args) {
        final Budget budget = new Budget(3);
        System.out.println(budget.tryReserve(2));
        System.out.println(budget.tryReserve(2));
        System.out.println(budget.remaining());
        final Object a = new Object(); final Object b = new Object();
        final AtomicStampedReference<Object> state = new AtomicStampedReference<>(a, 0);
        final int[] stamp = new int[1]; final Object observed = state.get(stamp);
        state.set(b, 1); state.set(a, 2);
        System.out.println(state.compareAndSet(observed, b, stamp[0], 3));
    }
}
