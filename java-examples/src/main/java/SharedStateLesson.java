/** Traces a lost update and repairs the complete operation with one monitor. @author Snehanjan Chatterjee */
public final class SharedStateLesson {
    private static final class Counter {
        private int value;
        /** Protects the whole read, addition and write with this counter's monitor. */
        private synchronized void increment() { value++; }
        /** Reads under the same monitor protocol. */
        private synchronized int value() { return value; }
    }
    /** Models one chosen schedule; these statements deliberately run on one thread. */
    public static int chosenLostUpdate() {
        int shared = 0;
        final int firstRead = shared;
        final int secondRead = shared;
        shared = firstRead + 1;
        shared = secondRead + 1;
        return shared;
    }
    /** Starts two finite workers and observes the counter only after both finish. */
    public static int countSafely(final int iterationsPerWorker) throws InterruptedException {
        if (iterationsPerWorker < 0) { throw new IllegalArgumentException("negative iteration count"); }
        Math.multiplyExact(iterationsPerWorker, 2);
        final Counter counter = new Counter();
        final Runnable task = () -> {
            for (int index = 0; index < iterationsPerWorker; index++) { counter.increment(); }
        };
        final Thread first = new Thread(task, "counter-first");
        final Thread second = new Thread(task, "counter-second");
        first.start();
        second.start();
        first.join();
        second.join();
        return counter.value();
    }
    /** Prints a model outcome and the completed real two-worker result. */
    public static void main(final String[] args) throws InterruptedException {
        System.out.println("chosen interleaving=" + chosenLostUpdate());
        System.out.println("synchronized=" + countSafely(1000));
    }
}
