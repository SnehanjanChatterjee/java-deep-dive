import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks bounded atomic transitions and contested reservations. @author Snehanjan Chatterjee */
public final class AtomicCounterLabTest {
    @Test public void testValidationAndBoundaries() {
        assertThrows(IllegalArgumentException.class, () -> new AtomicCounterLab.Budget(-1));
        final AtomicCounterLab.Budget budget = new AtomicCounterLab.Budget(3);
        assertThrows(IllegalArgumentException.class, () -> budget.tryReserve(0));
        assertThrows(IllegalArgumentException.class, () -> budget.tryReserve(-1));
        assertFalse(budget.tryReserve(4)); assertEquals(3, budget.remaining());
        assertTrue(budget.tryReserve(3)); assertFalse(budget.tryReserve(1)); assertEquals(0, budget.remaining());
    }
    @Test public void testConcurrentReservationConservesCapacity() throws InterruptedException {
        final AtomicCounterLab.Budget budget = new AtomicCounterLab.Budget(100);
        final AtomicInteger accepted = new AtomicInteger();
        final Runnable reserve = () -> { for (int i = 0; i < 200; i++) { if (budget.tryReserve(1)) { accepted.incrementAndGet(); } } };
        final Thread first = new Thread(reserve); final Thread second = new Thread(reserve);
        first.setDaemon(true); second.setDaemon(true); first.start(); second.start(); first.join(5000); second.join(5000);
        assertFalse(first.isAlive()); assertFalse(second.isAlive()); assertEquals(100, accepted.get()); assertEquals(0, budget.remaining());
    }
}
