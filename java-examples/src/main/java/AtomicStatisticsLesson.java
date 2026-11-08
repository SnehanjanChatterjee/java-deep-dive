import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/** Separates additive statistics and a maximum reduction from resource admission. @author Snehanjan Chatterjee */
public final class AtomicStatisticsLesson {
    /** Reads exact completed statistics after both contributing workers have joined. */
    public static long[] summarize() throws InterruptedException {
        final LongAdder total = new LongAdder();
        final LongAccumulator maximum = new LongAccumulator(Long::max, Long.MIN_VALUE);
        final Runnable contribute = () -> {
            for (long value = 1; value <= 3; value++) {
                total.add(value);
                maximum.accumulate(value);
            }
        };
        final Thread first = new Thread(contribute, "statistics-first");
        final Thread second = new Thread(contribute, "statistics-second");
        first.start();
        second.start();
        first.join();
        second.join();
        return new long[] {total.sum(), maximum.get()};
    }

    /** Prints the quiescent sum and maximum, making no throughput comparison. */
    public static void main(final String[] args) throws InterruptedException {
        final long[] statistics = summarize();
        System.out.println("sum=" + statistics[0]);
        System.out.println("maximum=" + statistics[1]);
    }
}
