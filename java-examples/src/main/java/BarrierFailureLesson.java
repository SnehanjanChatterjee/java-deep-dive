import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/** Forces one broken generation without sleeps or an indefinitely stranded peer. @author Snehanjan Chatterjee */
public final class BarrierFailureLesson {
    /** Interrupts one participant before await, then observes the broken generation from its peer. */
    public static String[] observations() throws InterruptedException {
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final String[] observed = new String[3];
        final Thread first = new Thread(() -> {
            Thread.currentThread().interrupt();
            try {
                barrier.await();
                throw new AssertionError("pre-interrupted await must not succeed");
            } catch (final InterruptedException expected) {
                observed[0] = "interrupted status=" + Thread.currentThread().isInterrupted();
            } catch (final BrokenBarrierException unexpected) {
                throw new AssertionError("first arrival should detect its own interruption", unexpected);
            }
        }, "broken-generation-participant");
        first.start();
        first.join();
        observed[1] = "broken=" + barrier.isBroken();
        try {
            barrier.await();
            throw new AssertionError("peer must observe the broken generation");
        } catch (final BrokenBarrierException expected) {
            observed[2] = "peer saw broken generation";
        }
        return observed;
    }

    /** Prints one interruption path followed by its consequence for a later arrival. */
    public static void main(final String[] args) throws InterruptedException {
        for (final String observation : observations()) {
            System.out.println(observation);
        }
    }
}
