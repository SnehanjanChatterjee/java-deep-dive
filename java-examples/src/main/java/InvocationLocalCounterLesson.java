import java.util.Objects;

/** Separates per-invocation parameters and indexes from one shared object's field. */
public final class InvocationLocalCounterLesson {
    public static final class Counter {
        private int value;
        /** Protects one complete increment with this Counter's monitor. */
        public synchronized void increment() { value++; }
        /** Observes the field through the same monitor protocol. */
        public synchronized int value() { return value; }
    }

    /** Receives a copied reference value and creates a fresh loop index per invocation. */
    public static void addTwice(final Counter counter) {
        Objects.requireNonNull(counter, "counter");
        for (int index = 0; index < 2; index++) {
            counter.increment();
        }
    }

    /** Prints the completed result after both invocations update the same Counter. */
    public static void main(final String[] args) throws InterruptedException {
        final Counter shared = new Counter();
        final Thread a = new Thread(() -> addTwice(shared), "A");
        final Thread b = new Thread(() -> addTwice(shared), "B");
        a.start(); b.start();
        a.join(); b.join();
        System.out.println("shared value=" + shared.value());
    }
}
