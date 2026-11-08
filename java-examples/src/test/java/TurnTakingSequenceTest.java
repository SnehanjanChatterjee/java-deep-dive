import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks turn ownership, partial final rounds, validation and owner cancellation. @author Snehanjan Chatterjee */
public final class TurnTakingSequenceTest {
    @Test(timeout = 5000) public void testOddEvenEndsAfterOddFinalValue() throws InterruptedException {
        assertEquals("odd=1 even=2 odd=3 even=4 odd=5", TurnTakingSequence.run(2, 5));
    }

    @Test(timeout = 5000) public void testThreeWorkersEndInPartialRound() throws InterruptedException {
        assertEquals("A=1 B=2 C=3 A=4 B=5 C=6 A=7 B=8", TurnTakingSequence.run(3, 8));
        assertEquals("A=1 B=2", TurnTakingSequence.run(3, 2));
    }

    @Test(timeout = 5000) public void testEmptyAndSingleElementLimits() throws InterruptedException {
        assertEquals("", TurnTakingSequence.run(2, 0));
        assertEquals("", TurnTakingSequence.run(3, 0));
        assertEquals("odd=1", TurnTakingSequence.run(2, 1));
        assertEquals("A=1", TurnTakingSequence.run(3, 1));
    }

    @Test public void testInvalidConfigurationDoesNotStartWorkers() {
        assertThrows(IllegalArgumentException.class, () -> TurnTakingSequence.run(1, 5));
        assertThrows(IllegalArgumentException.class, () -> TurnTakingSequence.run(4, 5));
        assertThrows(IllegalArgumentException.class, () -> TurnTakingSequence.run(2, -1));
    }

    @Test public void testPendingOwnerInterruptionIsReportedAndConsumed() {
        Thread.currentThread().interrupt();
        try {
            assertThrows(InterruptedException.class, () -> TurnTakingSequence.run(2, 5));
            assertFalse(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }

    @Test(timeout = 10000) public void testInterruptedOwnerCancelsAndJoinsParticipants() throws InterruptedException {
        final AtomicReference<Throwable> outcome = new AtomicReference<>();
        final Thread owner = new Thread(() -> {
            try {
                TurnTakingSequence.run(3, Integer.MAX_VALUE);
                outcome.set(new AssertionError("long sequence unexpectedly completed"));
            } catch (final InterruptedException expected) {
                outcome.set(expected);
            }
        }, "test-sequence-owner");
        owner.setDaemon(true);
        owner.start();
        try {
            final long deadline = System.nanoTime() + 2_000_000_000L;
            while (owner.getState() != Thread.State.WAITING && owner.isAlive()
                && System.nanoTime() - deadline < 0) {
                Thread.yield();
            }
            assertEquals(Thread.State.WAITING, owner.getState());
        } finally {
            owner.interrupt();
            owner.join(5000);
        }
        assertFalse(owner.isAlive());
        assertTrue(outcome.get() instanceof InterruptedException);
    }
}
