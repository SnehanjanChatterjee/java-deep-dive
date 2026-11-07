import java.util.concurrent.CountDownLatch;

/** Publishes independent result slots through a one-shot completion count. @author Snehanjan Chatterjee */
public final class LatchLesson {
    /** Waits for both finite workers before reading their ordinary array elements. */
    public static void main(final String[] args) throws InterruptedException {
        final int[] results = new int[2];
        final CountDownLatch done = new CountDownLatch(2);
        final Thread first = new Thread(() -> {
            try { results[0] = 20; }
            finally { done.countDown(); }
        });
        final Thread second = new Thread(() -> {
            try { results[1] = 22; }
            finally { done.countDown(); }
        });
        first.start();
        second.start();
        try {
            done.await();
            System.out.println("total=" + (results[0] + results[1]));
            System.out.println("remaining=" + done.getCount());
        } finally {
            first.join();
            second.join();
        }
    }
}
