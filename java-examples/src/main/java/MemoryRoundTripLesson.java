/** Publishes input through start and output through successful join. @author Snehanjan Chatterjee */
public final class MemoryRoundTripLesson {
    /** Doubles a pre-start ordinary input element and observes the post-join ordinary result element. */
    public static int calculate(final int input) throws InterruptedException {
        final int[] values = new int[2];
        values[0] = input;
        final Thread worker = new Thread(() -> values[1] = values[0] * 2, "round-trip-worker");
        worker.start();
        worker.join();
        return values[1];
    }

    /** Uses a small supported arithmetic input; int arithmetic otherwise follows Java overflow rules. */
    public static void main(final String[] args) throws InterruptedException {
        System.out.println(calculate(21));
    }
}
